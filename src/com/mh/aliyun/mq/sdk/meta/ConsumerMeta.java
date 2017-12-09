package com.mh.aliyun.mq.sdk.meta;

import java.io.Serializable;
import java.util.Properties;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.shade.com.alibaba.fastjson.annotation.JSONField;

public class ConsumerMeta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -893522000006166431L;

	/**
	 * 编号，唯一
	 */
	private String id;
	/**
	 * 对应阿里云配置的CID_开头的consumer名称
	 */
	private String name;
	/**
	 * 用来过滤获得特定标签的消息,全部种类用*标记
	 */
	private String tag;

	@JSONField(serialize = false)
	private String topicName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

}
