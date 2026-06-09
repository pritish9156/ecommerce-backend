package service;

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
import util.EmailUtil;

public class EmailService {
	
	private static Properties properties;
	
	static {
		
		InputStream inputStream = EmailService.class.getClassLoader()
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
	
	public static void sendVerificationEmail(
			String recipientEmail, String verificationToken) {
		
		String baseURL = properties.getProperty("app.base.url");
		
		String verificationLink = baseURL + "/auth/verify?token=" + verificationToken;
		
		String subject = "Verify Your ShopVerse Account";
		
		String body = "Welcome to ShopVerse.\n\n" +
			    "Please verify your account using the link below:\n\n" +
			    verificationLink;
		
		EmailUtil.sendEmail(recipientEmail, subject, body);
		
	}
	
	public static void sendPasswordResetEmail() {
		
	}
	
	public static void sendOrderConfirmationEmail() {
		
	}
	
	public static void sendOrderShippedEmail() {
		
	}

}
