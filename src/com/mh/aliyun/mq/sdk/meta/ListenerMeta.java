package com.mh.aliyun.mq.sdk.meta;

import java.io.Serializable;

public class ListenerMeta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3163363409145098012L;

	/**
	 * 编号，唯一
	 */
	private String id;
	/**
	 * 为哪个consumer服务的，对英国ConsumerMeta的ID值
	 */
	private String consumerRef;
	/**
	 * 初始化多少个线程
	 */
	private int initThreads;
	/**
	 * 初始化状态
	 */
	private String initStatus;
	/**
	 * 处理消息的实现类
	 */
	private String serviceClass;
	/**
	 * 线程池配置类
	 */
	private ExecutionPoolMeta executionPool;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getConsumerRef() {
		return consumerRef;
	}

	public void setConsumerRef(String consumerRef) {
		this.consumerRef = consumerRef;
	}

	public int getInitThreads() {
		return initThreads;
	}

	public void setInitThreads(int initThreads) {
		this.initThreads = initThreads;
	}

	public String getInitStatus() {
		return initStatus;
	}

	public void setInitStatus(String initStatus) {
		this.initStatus = initStatus;
	}

	public String getServiceClass() {
		return serviceClass;
	}

	public void setServiceClass(String serviceClass) {
		this.serviceClass = serviceClass;
	}

	public ExecutionPoolMeta getExecutionPool() {
		return executionPool;
	}

	public void setExecutionPool(ExecutionPoolMeta executionPool) {
		this.executionPool = executionPool;
	}

}
