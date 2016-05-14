package com.hg.leases.server;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class LeaseMaintenanceEmailHelper {

	private final String username;

	private final String password;

	private final String emailFrom;

	private final String emailTo;

	private final String emailCc;

	public LeaseMaintenanceEmailHelper(final String username,
			final String password, final String emailFrom,
			final String emailTo, final String emailCc) {
		this.username = username;
		this.password = password;
		this.emailFrom = emailFrom;
		this.emailTo = emailTo;
		this.emailCc = emailCc;
	}

	public void sendEmail(final String emailBody) throws AddressException,
			MessagingException {
		Session session = resolveEmailSession();
		Message message = new MimeMessage(session);
		message.setFrom(resolveEmailFrom());
		message.setRecipients(Message.RecipientType.TO, resolveEmailTo());
		message.setRecipients(Message.RecipientType.CC, resolveEmailCc());
		message.setSubject("JGS Leases Update");
		message.setText(emailBody);
		message.setContent(emailBody, "text/html; charset=utf-8");
		Transport.send(message);
	}

	private Session resolveEmailSession() {
		return Session.getInstance(resolveEmailProperties(),
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});
	}

	private Address resolveEmailFrom() throws AddressException {
		return new InternetAddress(emailFrom);
	}

	private Address[] resolveEmailTo() throws AddressException {
		return InternetAddress.parse(emailTo);
	}

	private Address[] resolveEmailCc() throws AddressException {
		return InternetAddress.parse(emailCc);
	}

	private Properties resolveEmailProperties() {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		return props;
	}

}