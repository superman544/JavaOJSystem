package cn.superman.web.util.mail;

import java.util.ArrayList;
import java.util.List;

/**
 * 邮件类，可以设置发件人，收件人，主题，文本内容，附加文字，抄送，暗送
 * 
 * @author 梁超人
 */
public class Mail {
	private String fromAddress;// 发件人
	private StringBuilder toAddress = new StringBuilder();// 收件人，用“，”分割
	private StringBuilder ccAddress = new StringBuilder();// 抄送
	private StringBuilder bccAddress = new StringBuilder();// 暗送
	private String subject;// 主题
	private String content;// 正文
	private List<AttachBean> attachList = null;// 附件列表

	public Mail(String fromAddress, String toAddress) {
		this.fromAddress = fromAddress;
		addToAddress(toAddress);
	}

	public Mail(String fromAddress, String toAddress, String subject,
			String content) {
		this.fromAddress = fromAddress;
		addToAddress(toAddress);
		this.subject = subject;
		this.content = content;
	}

	public Mail(String fromAddress, String toAddress, StringBuilder ccAddress,
			StringBuilder bccAddress, String subject, String content,
			List<AttachBean> attachList) {
		this.fromAddress = fromAddress;
		addToAddress(toAddress);
		this.ccAddress = ccAddress;
		this.bccAddress = bccAddress;
		this.subject = subject;
		this.content = content;
		this.attachList = attachList;
	}

	public void addToAddress(String address) {
		if (toAddress == null) {
			toAddress = new StringBuilder();
		}
		toAddress.append(address).append(",");
	}

	public void addCCAddress(String address) {
		if (ccAddress == null) {
			ccAddress = new StringBuilder();
		}
		ccAddress.append(address).append(",");
	}

	public void addBccAddress(String address) {
		if (bccAddress == null) {
			bccAddress = new StringBuilder();
		}
		bccAddress.append(address).append(",");
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getToAddress() {
		return toAddress.toString();
	}

	public void setToAddress(StringBuilder toAddress) {
		this.toAddress = toAddress;
	}

	public String getCcAddress() {
		return ccAddress.toString();
	}

	public void setCcAddress(StringBuilder ccAddress) {
		this.ccAddress = ccAddress;
	}

	public String getBccAddress() {
		return bccAddress.toString();
	}

	public void setBccAddress(StringBuilder bccAddress) {
		this.bccAddress = bccAddress;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<AttachBean> getAttachList() {
		return attachList;
	}

	public void setAttachList(List<AttachBean> attachList) {
		this.attachList = attachList;
	}

	public void addAttachBean(AttachBean attachBean) {
		if (attachList == null) {
			attachList = new ArrayList<AttachBean>();
		}
		attachList.add(attachBean);
	}

	public void removeAttachBean(AttachBean attachBean) {
		if (attachList != null && attachList.size() > 0) {
			attachList.remove(attachBean);
		}
	}
}
