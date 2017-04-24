package com.project1.zle;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author team pm1
 *
 *         Utilities to send and receive mails from a given mailbox
 */
public class EmailSender {

	private String host;
	private String from;
	private String pass;
	private javax.mail.Session session;

	/**
	 * MailUtil constructor
	 * 
	 * @param host
	 *            host for the connection
	 * 
	 * @param user
	 *            mail address of the mailbox
	 * 
	 * @param password
	 *            password of the mailbox
	 * 
	 * @throws IllegalArgumentException
	 *             if one or more argument is null or empty
	 */
	public EmailSender(String host, String user, String password) {
		super();
		if (host == null || user == null || password == null) {
			throw new IllegalArgumentException("Null argument: host or from or pass");
		}
		if (host.equals("") || user.equals("") || password.equals("")) {
			throw new IllegalArgumentException("Empty argument: host or from or pass");
		}

		this.host = host;
		this.from = user;
		this.pass = password;

		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.trust", host);
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.user", user);
		props.put("mail.smtp.password", password);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");

		session = javax.mail.Session.getDefaultInstance(props);
	}

	/**
	 * Email sender
	 * 
	 * @param mailto
	 *            recipient of the mail
	 * 
	 * @param subject
	 *            subject of the mail
	 * 
	 * @param content
	 *            content of the mail
	 * 
	 * @throws MessagingException
	 *             if there are something wrong in the message
	 */
	public void send(String mailto, String subject, String content) throws MessagingException {
		if (subject == null || content == null || mailto == null)
			throw new IllegalArgumentException("Null argument: subject or content or mailto");
		if (subject.equals("") || content.equals("") || mailto.equals(""))
			throw new IllegalArgumentException("Empty argument: subject or content or mailto");

		MimeMessage message = new MimeMessage(session);

		message.setFrom(new InternetAddress(from));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailto));
		message.setSubject(subject);
		message.setContent(content, "text/html");
		Transport transport = session.getTransport("smtp");
		transport.connect(host, from, pass);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
	}
}
