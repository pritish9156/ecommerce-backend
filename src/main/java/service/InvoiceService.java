package service;

import java.io.ByteArrayOutputStream;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import dao.OrderItemDAO;
import entity.Order;
import entity.OrderItem;

public class InvoiceService {

	private OrderItemDAO orderItemDAO;

	public InvoiceService() {
		orderItemDAO = new OrderItemDAO();
	}

	public byte[] generateInvoice(Order order) {

		try {

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			Document document = new Document();

			PdfWriter.getInstance(document, outputStream);

			document.open();

			document.add(new Paragraph("SHOPVERSE"));

			document.add(new Paragraph("TAX INVOICE"));

			document.add(new Paragraph(" "));

			document.add(new Paragraph("Order Number: " + order.getOrderNumber()));

			document.add(new Paragraph("Customer: " + order.getShippingFullName()));

			document.add(new Paragraph("Mobile: " + order.getShippingMobileNumber()));

			document.add(new Paragraph(
					"Shipping Address: " + order.getShippingAddressLine1() + ", " + order.getShippingCity() + ", "
							+ order.getShippingState() + " - " + order.getShippingPostalCode()));

			document.add(new Paragraph(" "));

			PdfPTable table = new PdfPTable(5);

			table.setWidthPercentage(100);

			table.addCell("Product");

			table.addCell("SKU");

			table.addCell("Price");

			table.addCell("Quantity");

			table.addCell("Subtotal");

			List<OrderItem> items = orderItemDAO.findByOrder(order);

			for (OrderItem item : items) {

				table.addCell(item.getProductVariant().getProduct().getName());

				table.addCell(item.getProductVariant().getSku());

				table.addCell("Rs. " + item.getPriceAtPurchase());

				table.addCell(String.valueOf(item.getQuantity()));

				table.addCell("Rs. " + item.getSubtotal());
			}

			document.add(table);

			document.add(new Paragraph(" "));

			document.add(new Paragraph("Total Amount: Rs. " + order.getTotalAmount()));

			document.add(new Paragraph("Payment Status: " + order.getPaymentStatus()));

			document.add(new Paragraph("Order Status: " + order.getOrderStatus()));

			document.add(new Paragraph(" "));

			document.add(new Paragraph("Thank you for shopping with ShopVerse."));

			document.close();

			return outputStream.toByteArray();

		} catch (Exception e) {

			e.printStackTrace();

			return null;
		}
	}
}