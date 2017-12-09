package com.mh.aliyun.mq.sdk.setup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AliyunMQMsgListenerFactory {
	private static Map<String, AliyunMQMsgListener> hmListenerThread = new HashMap<String, AliyunMQMsgListener>();

	public static void putListenerThread(String threadId, AliyunMQMsgListener listener) {
		hmListenerThread.put(threadId, listener);
	}

	public static AliyunMQMsgListener getListenerThread(String threadId) {
		return hmListenerThread.get(threadId);
	}

	public static List<AliyunMQMsgListener> listAllListenerThread() {
		List<AliyunMQMsgListener> list = new ArrayList<AliyunMQMsgListener>();
		if (!hmListenerThread.isEmpty()) {
			Set<String> setKey = hmListenerThread.keySet();
			for (String key : setKey) {
				list.add(hmListenerThread.get(key));
			}
		}
		return list;
	}

}
