package com.mh.aliyun.mq.sdk.config;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
	
	public static synchronized void initConfig(InputStream inputStream) throws IOException {
		if (configure == null) {
			String configString = inputStreamTOString(inputStream);
			configure = JSON.parseObject(configString, AliyunMQConfigure.class);
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
	
	public static String inputStreamTOString(InputStream in) throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int count = -1;
		while ((count = in.read(data, 0, 1024)) != -1)
			outStream.write(data, 0, count);

		data = null;
		return new String(outStream.toByteArray(), "utf-8");
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
