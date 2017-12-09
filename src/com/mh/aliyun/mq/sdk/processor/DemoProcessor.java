package com.mh.aliyun.mq.sdk.processor;

import com.aliyun.openservices.ons.api.Message;

public class DemoProcessor extends AbstractMessageProcessor {

	public DemoProcessor(Message message) {
		super(message);
	}
	
	

	@Override
	public void beforeProcess() throws Exception {
		System.out.println("invoked beforeProcess......");
	}



	@Override
	public void afterProcess() throws Exception {
		System.out.println("invoked afterProcess......");
	}



	@Override
	protected void process(String message) {
		System.out.println("received message::"+message);
	}

}
