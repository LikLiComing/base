package com.escredit.base.email.entity;

import java.util.Map;

public class MailRequest {

	/**
	 * 发件人
	 */
	private String from;

	/**
	 * 收件人
	 */
	private String to;

	/**
	 * 收件人称呼
	 */
	private String hello;

	/**
	 * 主题
	 */
	private String subject;

	/**
	 * 正文
	 */
	private String content;

	/**
	 * 模版中的变量
	 */
	private Map<String, Object> dataModel;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getHello() {
		return hello;
	}

	public void setHello(String hello) {
		this.hello = hello;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Map<String, Object> getDataModel() {
		return dataModel;
	}

	public void setDataModel(Map<String, Object> dataModel) {
		this.dataModel = dataModel;
	}
}
