package com.mh.aliyun.mq.sdk.meta;

import java.io.Serializable;
import java.util.List;

public class AliyunMQConfigure implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1510078083361592382L;

	/**
	 * 阿里云消息队列功能后台可查看
	 */
	private String accessKey;
	/**
	 * 阿里云消息队列功能后台可查看
	 */
	private String secretKey;
	/**
	 * 阿里云消息队列功能后台可查看
	 */
	private String accessUrl;

	/**
	 * 阿里云提供的SDK日志打印的目录
	 */
	private String logPath;

	/**
	 * 最多记录多少个日志，每个日志是64M
	 */
	private int logFileMaxIndex;

	/**
	 * 日志的级别 ERROR WARN DEBUG INFO
	 */
	private String logLevel;

	/**
	 * 阿里云消息队列功能配置的Topic
	 */
	private List<TopicMeta> topics;
	/**
	 * 监听消息的线程配置
	 */
	private List<ListenerMeta> listeners;

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getAccessUrl() {
		return accessUrl;
	}

	public void setAccessUrl(String accessUrl) {
		this.accessUrl = accessUrl;
	}

	public List<TopicMeta> getTopics() {
		return topics;
	}

	public void setTopics(List<TopicMeta> topics) {
		this.topics = topics;
	}

	public List<ListenerMeta> getListeners() {
		return listeners;
	}

	public void setListeners(List<ListenerMeta> listeners) {
		this.listeners = listeners;
	}

	public String getLogPath() {
		return logPath;
	}

	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}

	public int getLogFileMaxIndex() {
		return logFileMaxIndex;
	}

	public void setLogFileMaxIndex(int logFileMaxIndex) {
		this.logFileMaxIndex = logFileMaxIndex;
	}

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

}
