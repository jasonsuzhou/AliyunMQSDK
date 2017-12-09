package com.mh.aliyun.mq.sdk.setup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AliyunMQMsgSenderFactory {

	private static Map<String, AliyunMQMsgSender> hmSender = new HashMap<String, AliyunMQMsgSender>();

	public static AliyunMQMsgSender getSender(String producerId) {
		return hmSender.get(producerId);
	}

	public static void putSender(String producerId, AliyunMQMsgSender sender) {
		hmSender.put(producerId, sender);
	}

	public static List<AliyunMQMsgSender> listAllSender() {
		List<AliyunMQMsgSender> list = new ArrayList<AliyunMQMsgSender>();
		if (!hmSender.isEmpty()) {
			Set<String> setKey = hmSender.keySet();
			for (String key : setKey) {
				list.add(hmSender.get(key));
			}
		}
		return list;
	}

	public static Map<String, AliyunMQMsgSender> getHmSender() {
		return hmSender;
	}

	public static void setHmSender(Map<String, AliyunMQMsgSender> hmSender) {
		AliyunMQMsgSenderFactory.hmSender = hmSender;
	}

}
