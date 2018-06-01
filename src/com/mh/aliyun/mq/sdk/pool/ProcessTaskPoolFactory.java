package com.mh.aliyun.mq.sdk.pool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class ProcessTaskPoolFactory implements PooledObjectFactory<AliyunMQMsgProcessTask> {

	@Override
	public void activateObject(PooledObject<AliyunMQMsgProcessTask> pool) throws Exception {

	}

	@Override
	public void destroyObject(PooledObject<AliyunMQMsgProcessTask> pool) throws Exception {
		AliyunMQMsgProcessTask task = pool.getObject();
		if (task != null) {
			task = null;
		}
	}

	@Override
	public PooledObject<AliyunMQMsgProcessTask> makeObject() throws Exception {
		AliyunMQMsgProcessTask task = new AliyunMQMsgProcessTask();
		return new DefaultPooledObject<AliyunMQMsgProcessTask>(task);
	}

	@Override
	public void passivateObject(PooledObject<AliyunMQMsgProcessTask> pool) throws Exception {
	}

	@Override
	public boolean validateObject(PooledObject<AliyunMQMsgProcessTask> pool) {
		return true;
	}

}
