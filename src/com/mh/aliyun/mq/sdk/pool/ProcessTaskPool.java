package com.mh.aliyun.mq.sdk.pool;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class ProcessTaskPool {

	public static GenericObjectPool<AliyunMQMsgProcessTask> init(int maxPoolSize) {
		ProcessTaskPoolFactory factory = new ProcessTaskPoolFactory();
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMaxTotal(maxPoolSize);
		GenericObjectPool<AliyunMQMsgProcessTask> objectPool = new GenericObjectPool<AliyunMQMsgProcessTask>(factory,
				poolConfig);
		return objectPool;
	}

}
