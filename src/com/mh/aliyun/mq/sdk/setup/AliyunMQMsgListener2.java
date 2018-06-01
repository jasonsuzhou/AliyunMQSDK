package com.mh.aliyun.mq.sdk.setup;

import java.util.Properties;
import java.util.UUID;

import org.apache.commons.pool2.impl.GenericObjectPool;

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
import com.mh.aliyun.mq.sdk.pool.AliyunMQMsgProcessTask;
import com.mh.aliyun.mq.sdk.pool.ProcessTaskPool;

/**
 * use apache commons pool2 to implement task pool
 * @author miyao
 *
 */
public class AliyunMQMsgListener2 {

	private String threadId;
	private ListenerMeta listenerMeta;
	private ConsumerMeta consumerMeta;
	private Consumer consumer;
	private GenericObjectPool<AliyunMQMsgProcessTask> objectPool;
	private boolean hasRun = false;
	private volatile String status = "start";

	public AliyunMQMsgListener2(ListenerMeta listenerMeta, ConsumerMeta consumerMeta) {
		this.listenerMeta = listenerMeta;
		this.consumerMeta = consumerMeta;
		this.threadId = "ListenerId::" + listenerMeta.getId() + "::ConsumerId::" + consumerMeta.getId() + "::"
				+ UUID.randomUUID().toString();
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
					AliyunMQMsgProcessTask task = null;
					try {
						task = objectPool.borrowObject();
						task.setMessage(message);
						task.setServiceClass(listenerMeta.getServiceClass());
						task.beforeEexcute();
						task.run();
						task.afterEexcute();
						objectPool.returnObject(task);
						return Action.CommitMessage;
					} catch (Exception e) {
						return Action.ReconsumeLater;
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

	private void initThreadPool(ConsumerMeta consumerMeta) {
		int corePoolSize = listenerMeta.getExecutionPool().getMinSize();
		this.objectPool = ProcessTaskPool.init(corePoolSize);
	}

}
