package com.mh.aliyun.mq.sdk.processor;

import com.aliyun.openservices.ons.api.Message;

public abstract class AbstractMessageProcessor implements AliyunMQMessageProcessor {
	protected String key;
	protected String msgId;
	protected String message;
	protected String tag;

	public AbstractMessageProcessor(Message message) {
		try {
			this.key = message.getKey();
			this.msgId = message.getMsgID();
			this.tag = message.getTag();
			this.message = new String(message.getBody(), "utf-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void processMessage() throws Exception {
		process(this.message);
	}

	protected abstract void process(String message);

	@Override
	public void beforeProcess() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterProcess() throws Exception {
		// TODO Auto-generated method stub

	}

}
