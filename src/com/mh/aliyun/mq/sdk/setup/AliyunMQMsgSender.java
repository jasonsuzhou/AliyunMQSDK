package com.mh.aliyun.mq.sdk.setup;

import java.util.Properties;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.SendResult;
import com.mh.aliyun.mq.sdk.config.AliyunMQConfigReader;
import com.mh.aliyun.mq.sdk.meta.ProducerMeta;

public class AliyunMQMsgSender {

	private Producer producer;
	private ProducerMeta producerMeta;
	private String topicName;

	public AliyunMQMsgSender(ProducerMeta producerMeta, String topicName) {
		this.producerMeta = producerMeta;
		this.topicName = topicName;
		initProducer();
	}

	public SendResult sendMessage(String message) throws Exception {
		byte[] content = message.getBytes("utf-8");
		Message msg = new Message(topicName, "", content);
		SendResult result = null;
		try {
			result = producer.send(msg);
		} catch (Throwable e) {
			result = retrySendMessage(msg, producerMeta.getRetryTimes());
		}

		return result;
	}

	public SendResult sendMessage(String message, String msgKey) throws Exception {
		byte[] content = message.getBytes("utf-8");
		Message msg = new Message(topicName, "", msgKey, content);
		SendResult result = null;
		try {
			result = producer.send(msg);
		} catch (Throwable e) {
			result = retrySendMessage(msg, producerMeta.getRetryTimes());
		}
		return result;
	}

	/**
	 * 延迟发布消息
	 * 
	 * @param message
	 * @param tag
	 * @param delaySecond
	 *            延时时间 单位为秒
	 * @return
	 * @throws Exception
	 */
	public SendResult sendMessage(String message, String tag, int delaySecond) throws Exception {
		byte[] content = message.getBytes("utf-8");
		Message msg = new Message(topicName, tag, content);
		msg.setStartDeliverTime(System.currentTimeMillis() + (delaySecond * 1000));
		SendResult result = null;
		try {
			result = producer.send(msg);
		} catch (Throwable e) {
			result = retrySendMessage(msg, producerMeta.getRetryTimes());
		}
		return result;
	}

	private SendResult retrySendMessage(Message message, int retryTimes) {
		for (int i = 0; i < retryTimes; i++) {
			try {
				return producer.send(message);
			} catch (Throwable e) {
				// do nothing
			}
		}
		return null;
	}

	public void shutdown() {
		if (producer != null && producer.isStarted()) {
			producer.shutdown();
		}
	}

	private void initProducer() {
		Properties properties = new Properties();
		properties.put(PropertyKeyConst.ProducerId, producerMeta.getName());
		properties.put(PropertyKeyConst.AccessKey, AliyunMQConfigReader.getConfigure().getAccessKey());
		properties.put(PropertyKeyConst.SecretKey, AliyunMQConfigReader.getConfigure().getSecretKey());
		properties.put(PropertyKeyConst.SendMsgTimeoutMillis, producerMeta.getSendMsgTimeoutMillis());
		producer = ONSFactory.createProducer(properties);
		producer.start();
	}

}
