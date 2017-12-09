package com.mh.aliyun.mq.sdk.meta;

import java.io.Serializable;

public class TopicMeta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8202319041275757916L;

	/**
	 * 编号，唯一
	 */
	private String id;
	/**
	 * 对应阿里云消息队列后台配置的TOPIC名称
	 */
	private String name;
	/**
	 * Producer相关配置参数
	 */
	private ProducerMeta producer;
	/**
	 * Consumer相关配置参数
	 */
	private ConsumerMeta consumers;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ProducerMeta getProducer() {
		return producer;
	}

	public void setProducer(ProducerMeta producer) {
		this.producer = producer;
	}

	public ConsumerMeta getConsumers() {
		return consumers;
	}

	public void setConsumers(ConsumerMeta consumers) {
		this.consumers = consumers;
	}

}
