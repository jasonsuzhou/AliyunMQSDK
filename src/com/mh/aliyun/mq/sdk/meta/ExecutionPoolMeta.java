package com.mh.aliyun.mq.sdk.meta;

import java.io.Serializable;

public class ExecutionPoolMeta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1697498220249976074L;

	/**
	 * 最小线程数
	 */
	private int minSize;
	/**
	 * 等待线程数
	 */
	private int waitSize;
	/**
	 * 最大线程数
	 */
	private int maxSize;
	/**
	 * 保持活跃的时间，单位为毫秒
	 */
	private int keepAliveTime;

	public int getMinSize() {
		return minSize;
	}

	public void setMinSize(int minSize) {
		this.minSize = minSize;
	}

	public int getWaitSize() {
		return waitSize;
	}

	public void setWaitSize(int waitSize) {
		this.waitSize = waitSize;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public int getKeepAliveTime() {
		return keepAliveTime;
	}

	public void setKeepAliveTime(int keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}

}
