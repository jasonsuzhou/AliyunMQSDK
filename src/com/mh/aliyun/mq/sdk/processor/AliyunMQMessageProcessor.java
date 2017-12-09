package com.mh.aliyun.mq.sdk.processor;

public interface AliyunMQMessageProcessor {
	
	void beforeProcess() throws Exception;
	
	void processMessage() throws Exception;

	void afterProcess() throws Exception;

}
