package com.mh.aliyun.mq.sdk.pool;

import java.lang.reflect.Constructor;

import com.aliyun.openservices.ons.api.Message;
import com.mh.aliyun.mq.sdk.processor.AliyunMQMessageProcessor;

public class AliyunMQMsgProcessTask extends Thread {

	private AliyunMQMessageProcessor processor;
	private Message message;
	private String serviceClass;

	public void init() {
		if (this.serviceClass != null && this.serviceClass.length() > 0) {
			try {
				Class clazz = Class.forName(this.serviceClass);
				Class[] paramTypes = { Message.class };
				Object[] params = { this.message };
				Constructor con = clazz.getConstructor(paramTypes);
				processor = (AliyunMQMessageProcessor) con.newInstance(params);
			} catch (Exception e) {
				// TODO may need log
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("serviceClass not found in the configuration file!");
		}
	}

	public void beforeEexcute() {
		if (processor != null) {
			try {
				processor.beforeProcess();
			} catch (Exception e) {
				// TODO may need log
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		if (processor != null) {
			try {
				processor.processMessage();
			} catch (Exception e) {
				// TODO may need log
				e.printStackTrace();
			}
		}
	}

	public void afterEexcute() {
		if (processor != null) {
			try {
				processor.afterProcess();
			} catch (Exception e) {
				// TODO may need log
				e.printStackTrace();
			}
		}
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public String getServiceClass() {
		return serviceClass;
	}

	public void setServiceClass(String serviceClass) {
		this.serviceClass = serviceClass;
	}

}
