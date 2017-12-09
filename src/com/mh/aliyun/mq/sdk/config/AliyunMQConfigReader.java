package com.mh.aliyun.mq.sdk.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;
import com.mh.aliyun.mq.sdk.meta.AliyunMQConfigure;
import com.mh.aliyun.mq.sdk.meta.ListenerMeta;

public class AliyunMQConfigReader {

	private static AliyunMQConfigure configure = null;

	public static synchronized void initConfig(File configFile) throws IOException {
		if (configure == null) {
			byte[] configBytes = getContent(configFile);
			configure = JSON.parseObject(new String(configBytes), AliyunMQConfigure.class);
		}
	}

	public static AliyunMQConfigure getConfigure() {
		return configure;
	}

	public static List<ListenerMeta> getListenerList() {
		if (configure != null) {
			return configure.getListeners();
		}
		return null;
	}

	public static boolean hasInit() {
		return configure != null;
	}

	private static byte[] getContent(File file) throws IOException {
		long fileSize = file.length();
		if (fileSize > Integer.MAX_VALUE) {
			return null;
		}
		FileInputStream fi = new FileInputStream(file);
		byte[] buffer = new byte[(int) fileSize];
		int offset = 0;
		int numRead = 0;
		while (offset < buffer.length && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
			offset += numRead;
		}
		// 确保所有数据均被读取
		if (offset != buffer.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}
		fi.close();
		return buffer;
	}

}
