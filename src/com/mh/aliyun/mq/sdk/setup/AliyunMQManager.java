package com.mh.aliyun.mq.sdk.setup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mh.aliyun.mq.sdk.config.AliyunMQConfigReader;
import com.mh.aliyun.mq.sdk.meta.AliyunMQConfigure;
import com.mh.aliyun.mq.sdk.meta.ConsumerMeta;
import com.mh.aliyun.mq.sdk.meta.ListenerMeta;
import com.mh.aliyun.mq.sdk.meta.ProducerMeta;
import com.mh.aliyun.mq.sdk.meta.TopicMeta;

public class AliyunMQManager {

	private static Map<String, ConsumerMeta> hmConsumerMeta = new HashMap<String, ConsumerMeta>();
	private static Map<String, ListenerMeta> hmListenerMeta = new HashMap<String, ListenerMeta>();

	/**
	 * 初始化配置文件后可以调用此方法，想要使用MQ收发消息之前该方法必须调用
	 */
	public static void begin() {
		if (!AliyunMQConfigReader.hasInit()) {
			throw new RuntimeException(
					"AliyunMQConfigReader.initConfig(File configFile) not been invoked!Please invoke first! ");
		}
		AliyunMQConfigure configure = AliyunMQConfigReader.getConfigure();
		System.setProperty("ons.client.logRoot", configure.getLogPath());
		System.setProperty("ons.client.logFileMaxIndex", String.valueOf(configure.getLogFileMaxIndex()));
		System.setProperty("ons.client.logLevel ", configure.getLogLevel());
		List<TopicMeta> listTopic = configure.getTopics();
		if (listTopic != null && !listTopic.isEmpty()) {
			ConsumerMeta consumerMeta = null;
			for (TopicMeta topic : listTopic) {
				consumerMeta = topic.getConsumers();
				if (consumerMeta != null) {
					consumerMeta.setTopicName(topic.getName());
					hmConsumerMeta.put(topic.getConsumers().getId(), topic.getConsumers());
				}
				initProducers(topic.getProducer(), topic.getName());
			}
		}
		initListeners(hmConsumerMeta);
	}

	/**
	 * 服务器关闭或者想手动停止收发消息的线程，调用该方法
	 */
	public static void end() {
		shutdownProducers();
		shutdownListeners();
	}

	/**
	 * 如果配置了监听器初始化状态为stop，那么后续程序可以通过哦调用该方法启动监听器接收消息
	 * 
	 * @param listenerId
	 */
	public static void starListener(String listenerId) {
		List<AliyunMQMsgListener> listenerInstanceList = AliyunMQMsgListenerFactory.listAllListenerThread();
		if (listenerInstanceList != null && !listenerInstanceList.isEmpty()) {
			String threadId = null;
			for (AliyunMQMsgListener listener : listenerInstanceList) {
				threadId = listener.getThreadId();
				if (threadId.startsWith("ListenerId::" + listener.getId() + "::") && !listener.isHasRun()) {
					listener.run();
				}
			}
		}
	}

	/**
	 * 该方法会把对应的监听器线程全部关闭
	 * 
	 * @param listenerId
	 */
	public static void stopListener(String listenerId) {
		List<AliyunMQMsgListener> listenerInstanceList = AliyunMQMsgListenerFactory.listAllListenerThread();
		if (listenerInstanceList != null && !listenerInstanceList.isEmpty()) {
			String threadId = null;
			for (AliyunMQMsgListener listener : listenerInstanceList) {
				threadId = listener.getThreadId();
				if (threadId.startsWith("ListenerId::" + listener.getId() + "::") && listener.isHasRun()) {
					listener.shutdown();
				}
			}
		}
	}

	/**
	 * 为监听器新增线程来处理
	 * 
	 * @param listenerId
	 *            监听器ID
	 * @param threadSize
	 *            新增多少个线程
	 */
	public static void addNewListenerThread(String listenerId, int threadSize) {
		for (int i = 0; i < threadSize; i++) {
			addOneListenerThread(listenerId);
		}
	}

	/**
	 * 为监听器新增一个线程来处理
	 * @param listenerId  监听器ID
	 * @return 返回新增的线程ID
	 */
	public static String addOneListenerThread(String listenerId) {
		ListenerMeta listenerMeta = hmListenerMeta.get(listenerId);
		ConsumerMeta consumerMeta = hmConsumerMeta.get(listenerMeta.getConsumerRef());
		AliyunMQMsgListener listener = new AliyunMQMsgListener(listenerMeta, consumerMeta);
		listener.run();
		String threadId = listener.getThreadId();
		AliyunMQMsgListenerFactory.putListenerThread(threadId, listener);
		return threadId;
	}

	/**
	 * 获得所有监听器线程列表
	 * 
	 * @return
	 */
	public static List<AliyunMQMsgListener> listAllMsgListenerThread() {
		return AliyunMQMsgListenerFactory.listAllListenerThread();
	}

	/**
	 * 关闭某个监听器线程
	 * 
	 * @param threadId
	 */
	public static void stopListenerThread(String threadId) {
		AliyunMQMsgListener listener = AliyunMQMsgListenerFactory.getListenerThread(threadId);
		if (listener != null) {
			listener.shutdown();
		}
	}
	
	/**
	 * 获得发送消息的工具类发送消息
	 * @param producerId 配置文件中配置的producer id
	 * @return
	 */
	public static AliyunMQMsgSender getSender(String producerId) {
		return AliyunMQMsgSenderFactory.getSender(producerId);
	}

	private static void initListeners(Map<String, ConsumerMeta> hmConsumer) {
		List<ListenerMeta> listListenerMeta = AliyunMQConfigReader.getListenerList();
		if (listListenerMeta!= null && !listListenerMeta.isEmpty()) {
			ConsumerMeta consumerMeta = null;
			for (ListenerMeta listenerMeta : listListenerMeta) {
				hmListenerMeta.put(listenerMeta.getId(), listenerMeta);
				consumerMeta = hmConsumer.get(listenerMeta.getConsumerRef());
				int initThreads = listenerMeta.getInitThreads();
				for (int i = 0; i < initThreads; i++) {
					AliyunMQMsgListener listener = new AliyunMQMsgListener(listenerMeta, consumerMeta);
					if ("start".equalsIgnoreCase(listenerMeta.getInitStatus())) {
						listener.run();
					}
					AliyunMQMsgListenerFactory.putListenerThread(listener.getThreadId(), listener);
				}
			}
		}
	}

	private static void initProducers(ProducerMeta producer, final String topicName) {
		if (producer != null) {
			AliyunMQMsgSender sender = new AliyunMQMsgSender(producer, topicName);
			AliyunMQMsgSenderFactory.putSender(producer.getId(), sender);
		}
	}

	private static void shutdownProducers() {
		List<AliyunMQMsgSender> list = AliyunMQMsgSenderFactory.listAllSender();
		for (AliyunMQMsgSender sender : list) {
			sender.shutdown();
		}
	}

	private static void shutdownListeners() {
		List<AliyunMQMsgListener> list = AliyunMQMsgListenerFactory.listAllListenerThread();
		if (list != null && !list.isEmpty()) {
			for (AliyunMQMsgListener listener : list) {
				listener.shutdown();
			}
		}
	}

}
