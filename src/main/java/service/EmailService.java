package service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import entity.Order;
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

		InputStream inputStream = EmailService.class.getClassLoader().getResourceAsStream("application.properties");

		if (inputStream == null) {
			throw new RuntimeException("Sorry, unable to find application.properties");
		} else {
			properties = new Properties();

			try {
				properties.load(inputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void sendVerificationEmail(String recipientEmail, String verificationToken) {

		String baseURL = properties.getProperty("app.base.url");

		String verificationLink = baseURL + "/auth/verify?token=" + verificationToken;

		String subject = "Verify Your ShopVerse Account";

		String body = "<html>" + "<body>" + "<h2>Welcome to Your Store</h2>" + "<p>Thank you for registering.</p>"
				+ "<p>Please verify your email address by clicking the button below:</p>" +

				"<a href='" + verificationLink + "' " + "style='display:inline-block;" + "padding:12px 24px;"
				+ "background:#2563eb;" + "color:#ffffff;" + "text-decoration:none;" + "border-radius:6px;"
				+ "font-weight:bold;'>" + "Verify Email" + "</a>" +

				"<p>This link will expire after the specified time.</p>"
				+ "<p>If you did not create this account, you can safely ignore this email.</p>" + "</body>"
				+ "</html>";

		EmailUtil.sendEmail(recipientEmail, subject, body);

	}

	public static void sendOrderConfirmationEmail(Order order) {

		String recipientEmail = order.getUser().getEmail();

		String subject = "Order Confirmed - " + order.getOrderNumber();

		String body = "Hello " + order.getShippingFullName() + ",\n\n" +

				"Thank you for shopping with ShopVerse.\n\n" +

				"Your order has been placed successfully.\n\n" +

				"Order Number: " + order.getOrderNumber() + "\n" + "Order Status: " + order.getOrderStatus() + "\n"
				+ "Total Amount: ₹" + order.getTotalAmount() + "\n\n" +

				"We will notify you when your order status changes.\n\n" +

				"Thank you,\n" + "ShopVerse Team";

		EmailUtil.sendEmail(recipientEmail, subject, body);
	}

	public static void sendPaymentPendingEmail(Order order) {

		String recipientEmail = order.getUser().getEmail();

		String subject = "Payment Pending - " + order.getOrderNumber();

		String body = "Hello " + order.getShippingFullName() + ",\n\n" +

				"Your order has been created, but the payment is currently pending.\n\n" +

				"Order Number: " + order.getOrderNumber() + "\n" + "Amount: ₹" + order.getTotalAmount() + "\n\n" +

				"Please complete the payment to confirm your order.\n\n" +

				"Thank you,\n" + "ShopVerse Team";

		EmailUtil.sendEmail(recipientEmail, subject, body);
	}

	public static void sendPaymentSuccessEmail(Order order) {

		String recipientEmail = order.getUser().getEmail();

		String subject = "Payment Successful - " + order.getOrderNumber();

		String body = "Hello " + order.getShippingFullName() + ",\n\n" +

				"Your payment has been received successfully.\n\n" +

				"Order Number: " + order.getOrderNumber() + "\n" + "Amount Paid: ₹" + order.getTotalAmount() + "\n\n" +

				"Your order is now confirmed and will be processed shortly.\n\n" +

				"Thank you for shopping with ShopVerse.\n\n" +

				"ShopVerse Team";

		EmailUtil.sendEmail(recipientEmail, subject, body);
	}

	public static void sendOrderCancelledEmail(Order order) {

		String recipientEmail = order.getUser().getEmail();

		String subject = "Order Cancelled - " + order.getOrderNumber();

		String body = "Hello " + order.getShippingFullName() + ",\n\n" +

				"Your order has been cancelled successfully.\n\n" +

				"Order Number: " + order.getOrderNumber() + "\n" + "Order Status: CANCELLED\n\n" +

				"If payment was already completed, any applicable refund will be processed according to our refund policy.\n\n"
				+

				"ShopVerse Team";

		EmailUtil.sendEmail(recipientEmail, subject, body);
	}

	public static void sendOrderStatusUpdateEmail(Order order) {

		String recipientEmail = order.getUser().getEmail();

		String status = order.getOrderStatus().name().replace("_", " ");

		String subject = "Order Update - " + order.getOrderNumber();

		String body = "Hello " + order.getShippingFullName() + ",\n\n" +

				"Your order status has been updated.\n\n" +

				"Order Number: " + order.getOrderNumber() + "\n" + "Current Status: " + status + "\n\n" +

				"You can check your order details for the latest updates.\n\n" +

				"Thank you,\n" + "ShopVerse Team";

		EmailUtil.sendEmail(recipientEmail, subject, body);
	}

	public static void sendInvoiceEmail(Order order, byte[] invoicePdf) {

		String recipientEmail = order.getUser().getEmail();

		String subject = "Your ShopVerse Invoice - " + order.getOrderNumber();

		String body = "Hello " + order.getShippingFullName() + ",\n\n"
				+ "Your order has been delivered successfully.\n\n"
				+ "Please find your invoice attached to this email.\n\n" + "Order Number: " + order.getOrderNumber()
				+ "\n" + "Total Amount: Rs. " + order.getTotalAmount() + "\n\n"
				+ "Thank you for shopping with ShopVerse.\n\n" + "ShopVerse Team";

		String fileName = "Invoice-" + order.getOrderNumber() + ".pdf";

		EmailUtil.sendEmailWithAttachment(recipientEmail, subject, body, invoicePdf, fileName);
	}

}
