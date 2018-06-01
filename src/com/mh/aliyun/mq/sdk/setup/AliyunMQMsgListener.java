package com.mh.aliyun.mq.sdk.setup;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.mh.aliyun.mq.sdk.config.AliyunMQConfigReader;
import com.mh.aliyun.mq.sdk.meta.ConsumerMeta;
import com.mh.aliyun.mq.sdk.meta.ListenerMeta;

public class AliyunMQMsgListener {

	private String threadId;
	private ListenerMeta listenerMeta;
	private ConsumerMeta consumerMeta;
	private Consumer consumer;
	private ThreadPoolExecutor tpe;
	private long totalMsg;
	private long maxPoolSize;
	private boolean hasRun = false;
	private volatile String status = "start";

	public AliyunMQMsgListener(ListenerMeta listenerMeta, ConsumerMeta consumerMeta) {
		this.listenerMeta = listenerMeta;
		this.consumerMeta = consumerMeta;
		this.threadId = "ListenerId::" + listenerMeta.getId() + "::ConsumerId::" + consumerMeta.getId() + "::"
				+ UUID.randomUUID().toString();
		this.totalMsg = 0;
		this.maxPoolSize = listenerMeta.getExecutionPool().getMaxSize();
		this.initThreadPool(consumerMeta);
	}

	public synchronized void run() {
		if (hasRun) {
			return;
		}
		Properties properties = new Properties();
		properties.put(PropertyKeyConst.ConsumerId, consumerMeta.getName());
		properties.put(PropertyKeyConst.AccessKey, AliyunMQConfigReader.getConfigure().getAccessKey());
		properties.put(PropertyKeyConst.SecretKey, AliyunMQConfigReader.getConfigure().getSecretKey());
		final String topicName = this.consumerMeta.getTopicName();
		final String tag = consumerMeta.getTag();
		consumer = ONSFactory.createConsumer(properties);
		consumer.subscribe(topicName, tag, new MessageListener() {
			public Action consume(Message message, ConsumeContext context) {
				if (isNeedRun()) {
					++totalMsg;
					AliyunMQMsgProcessTask task = new AliyunMQMsgProcessTask(message, listenerMeta.getServiceClass());
					task.beforeEexcute();
					tpe.execute(task);
					task.afterEexcute();
					while (true) {
						if (hasFreeThread()) {
							return Action.CommitMessage;
						} else {
							doSleep(1000);
						}
					}
				} else {
					return Action.ReconsumeLater;
				}
			}

			private boolean isNeedRun() {
				return "start".equalsIgnoreCase(getStatus());
			}
		});
		consumer.start();
		hasRun = true;
	}

	public void shutdown() {
		if (consumer != null && consumer.isStarted()) {
			this.setStatus("stop");
			consumer.shutdown();
		}
	}

	private void doSleep(int mills) {
		try {
			Thread.sleep(mills);
		} catch (InterruptedException e) {
			// do nothing
		}
	}
	
	public String getThreadId() {
		return threadId;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public boolean isHasRun() {
		return hasRun;
	}
	
	public int getInitThreads() {
		return listenerMeta.getInitThreads();
	}
	
	public String getId() {
		return listenerMeta.getId();
	}

	private boolean hasFreeThread() {
		int activeSize = tpe.getActiveCount();
		long completeSize = tpe.getCompletedTaskCount();
		if (totalMsg - completeSize >= maxPoolSize || activeSize >= maxPoolSize) {
			return false;
		} else {
			return true;
		}
	}

	private void initThreadPool(ConsumerMeta consumerMeta) {
		int corePoolSize = listenerMeta.getExecutionPool().getMinSize();
		int maximumPoolSize = listenerMeta.getExecutionPool().getMaxSize();
		int keepAliveTime = listenerMeta.getExecutionPool().getKeepAliveTime();
		int queueSize = listenerMeta.getExecutionPool().getWaitSize();
		BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(queueSize);
		RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
		this.tpe = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS,
				workQueue, handler);
	}

}
