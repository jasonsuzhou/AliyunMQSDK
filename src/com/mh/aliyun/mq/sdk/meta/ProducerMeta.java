package com.mh.aliyun.mq.sdk.meta;

import java.io.Serializable;

public class ProducerMeta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -694338421329360606L;

	/**
	 * 编号，唯一
	 */
	private String id;
	/**
	 * 对应阿里云上配置的PID_开头的Producer名称
	 */
	private String name;
	/**
	 * 发送消息失败后的重试次数
	 */
	private int retryTimes;

	/**
	 * 发送消息超时时间，单位为毫秒
	 */
	private int sendMsgTimeoutMillis;

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

	public int getRetryTimes() {
		return retryTimes;
	}

	public void setRetryTimes(int retryTimes) {
		this.retryTimes = retryTimes;
	}

	public int getSendMsgTimeoutMillis() {
		return sendMsgTimeoutMillis;
	}

	public void setSendMsgTimeoutMillis(int sendMsgTimeoutMillis) {
		this.sendMsgTimeoutMillis = sendMsgTimeoutMillis;
	}

}
