package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.MimeMessage;
import service.EmailService;

public class EmailUtil {
	
	private static Properties properties;
	
	static {
		
		InputStream inputStream = EmailUtil.class.getClassLoader()
				.getResourceAsStream("application.properties");
		
		if (inputStream == null) {
            throw new RuntimeException("Sorry, unable to find application.properties");
        }
		else {
			properties = new Properties();
			
			try {
				properties.load(inputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void sendEmail(String recipientEmail, String subject, String body) {
		
		String username = properties.getProperty("mail.username");
		
		String password = properties.getProperty("mail.password");
			
		Properties smtpProperties = new Properties();

		smtpProperties.put(
		        "mail.smtp.host",
		        properties.getProperty("mail.smtp.host"));

		smtpProperties.put(
		        "mail.smtp.port",
		        properties.getProperty("mail.smtp.port"));

		smtpProperties.put(
		        "mail.smtp.auth",
		        properties.getProperty("mail.smtp.auth"));

		smtpProperties.put(
		        "mail.smtp.starttls.enable",
		        properties.getProperty("mail.smtp.starttls.enable"));
		
		Session session = Session.getInstance(
		        smtpProperties,
		        new Authenticator() {

		            @Override
		            protected PasswordAuthentication getPasswordAuthentication() {

		                return new PasswordAuthentication(
		                        username,
		                        password
		                );
		            }

		        });
		
		MimeMessage message = new MimeMessage(session);
		
		try {
			message.setFrom(username);
			message.setRecipients(Message.RecipientType.TO, recipientEmail);
			message.setSubject(subject);
			message.setText(body);
			
			Transport.send(message);
		} catch (MessagingException e) {
			System.err.println("Failed to send email.");
			e.printStackTrace();
		}
		
	}

}
