package cn.superman.web.util.mail;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * 邮件发送工具类
 * 
 * @author 梁超人
 */
public class SendMailUtil {
	/**
	 * 创建一个默认的的发送邮件的Session
	 */
	public static Session createDefaultSession(String host,
			final String username, final String password) {
		Properties prop = new Properties();
		prop.setProperty("mail.host", host);// 指定主机
		prop.setProperty("mail.smtp.auth", "true");// 指定验证为true
		prop.setProperty("mail.transport.protocol", "smtp");
		// 不使用普通的socket，不开这个的话，会报SSL530错误
		prop.put("mail.smtp.starttls.enable", "true");

		// 创建验证器
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		};

		// 获取session对象
		return Session.getInstance(prop, auth);
	}

	/**
	 * 发送邮件，传入一个电子邮件，一个发送session，以及文件文本的编码
	 * 
	 * @param mail
	 * @param session
	 * @param charSet
	 * @throws AddressException
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static void sendMail(Mail mail, Session session, String charSet)
			throws AddressException, MessagingException, IOException {

		MimeMessage msg = new MimeMessage(session);// 创建邮件对象
		msg.setSubject(mail.getSubject());// 设置主题
		msg.setFrom(new InternetAddress(mail.getFromAddress()));// 设置发件人
		msg.addRecipients(RecipientType.TO, mail.getToAddress());// 设置收件人
		// 设置抄送
		String cc = mail.getCcAddress();
		if (!cc.isEmpty() && cc != null) {
			msg.addRecipients(RecipientType.CC, cc);
		}

		// 设置暗送
		String bcc = mail.getBccAddress();
		if (!bcc.isEmpty() && bcc != null) {
			msg.addRecipients(RecipientType.BCC, bcc);
		}
		// 创建部件集对象
		MimeMultipart parts = new MimeMultipart();
		// 文字内容
		MimeBodyPart wordPart = new MimeBodyPart();
		wordPart.setContent(mail.getContent(), "text/html;charset=" + charSet);
		parts.addBodyPart(wordPart);

		// 添加邮件附件
		MimeBodyPart filePart = null;
		List<AttachBean> attachList = mail.getAttachList();

		if (attachList != null && attachList.size() > 0) {
			// 添加附件
			for (AttachBean bean : attachList) {
				filePart = new MimeBodyPart();

				filePart.setDataHandler(new DataHandler(new FileDataSource(bean
						.getPath())));
				filePart.setContentID("1.jpg");
				filePart.setFileName(MimeUtility.encodeText(bean.getFileName()));
				;

				parts.addBodyPart(filePart);
			}
		}
		msg.setContent(parts);// 给邮件设置内容
		// session.setDebug(true);
		Transport.send(msg);
	}
}
