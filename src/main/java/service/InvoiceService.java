package service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import dao.OrderItemDAO;
import dto.response.PaymentResponseDTO;
import entity.Order;
import entity.OrderItem;
import entity.Payment;

public class InvoiceService {

	private OrderItemDAO orderItemDAO;
	private PaymentService paymentService;

	// =========================================================
	// COLORS
	// =========================================================

	private static final java.awt.Color PRIMARY = new java.awt.Color(37, 99, 235);

	private static final java.awt.Color DARK = new java.awt.Color(15, 23, 42);

	private static final java.awt.Color MUTED = new java.awt.Color(100, 116, 139);

	private static final java.awt.Color LIGHT = new java.awt.Color(248, 250, 252);

	private static final java.awt.Color BORDER = new java.awt.Color(226, 232, 240);

	private static final java.awt.Color WHITE = java.awt.Color.WHITE;

	// =========================================================
	// FONTS
	// =========================================================

	private static final Font LOGO_FONT = new Font(Font.HELVETICA, 24, Font.BOLD, PRIMARY);

	private static final Font TITLE_FONT = new Font(Font.HELVETICA, 20, Font.BOLD, DARK);

	private static final Font SECTION_FONT = new Font(Font.HELVETICA, 11, Font.BOLD, DARK);

	private static final Font NORMAL_FONT = new Font(Font.HELVETICA, 9, Font.NORMAL, DARK);

	private static final Font MUTED_FONT = new Font(Font.HELVETICA, 8, Font.NORMAL, MUTED);

	private static final Font SMALL_BOLD_FONT = new Font(Font.HELVETICA, 8, Font.BOLD, DARK);

	private static final Font TABLE_HEADER_FONT = new Font(Font.HELVETICA, 8, Font.BOLD, WHITE);

	private static final Font TOTAL_FONT = new Font(Font.HELVETICA, 13, Font.BOLD, PRIMARY);

	public InvoiceService() {

		orderItemDAO = new OrderItemDAO();
		paymentService = new PaymentService();
	}

	// =========================================================
	// GENERATE INVOICE
	// =========================================================

	public byte[] generateInvoice(Order order) {

		try {

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			Document document = new Document(PageSize.A4, 36, 36, 40, 40);

			PdfWriter.getInstance(document, outputStream);

			document.open();

			// =================================================
			// HEADER
			// =================================================

			PdfPTable headerTable = new PdfPTable(2);

			headerTable.setWidthPercentage(100);

			headerTable.setWidths(new float[] { 60, 40 });

			PdfPCell logoCell = new PdfPCell();

			logoCell.setBorder(Rectangle.NO_BORDER);

			Paragraph logo = new Paragraph("SHOPVERSE", LOGO_FONT);

			logo.setSpacingAfter(3);

			logoCell.addElement(logo);

			Paragraph tagline = new Paragraph("Premium Shopping Experience", MUTED_FONT);

			logoCell.addElement(tagline);

			headerTable.addCell(logoCell);

			PdfPCell invoiceCell = new PdfPCell();

			invoiceCell.setBorder(Rectangle.NO_BORDER);

			Paragraph invoiceTitle = new Paragraph("TAX INVOICE", TITLE_FONT);

			invoiceTitle.setAlignment(Element.ALIGN_RIGHT);

			invoiceCell.addElement(invoiceTitle);

			Paragraph invoiceNumber = new Paragraph("Invoice #" + order.getOrderNumber(), SMALL_BOLD_FONT);

			invoiceNumber.setAlignment(Element.ALIGN_RIGHT);

			invoiceCell.addElement(invoiceNumber);

			if (order.getCreatedAt() != null) {

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

				Paragraph date = new Paragraph("Date: " + order.getCreatedAt().format(formatter), MUTED_FONT);

				date.setAlignment(Element.ALIGN_RIGHT);

				invoiceCell.addElement(date);
			}

			headerTable.addCell(invoiceCell);

			document.add(headerTable);

			// =================================================
			// BLUE LINE
			// =================================================

			PdfPTable lineTable = new PdfPTable(1);

			lineTable.setWidthPercentage(100);

			PdfPCell lineCell = new PdfPCell();

			lineCell.setBorder(Rectangle.NO_BORDER);

			lineCell.setFixedHeight(4);

			lineCell.setBackgroundColor(PRIMARY);

			lineTable.addCell(lineCell);

			document.add(lineTable);

			document.add(new Paragraph(" "));

			// =================================================
			// BILLING / SHIPPING INFORMATION
			// =================================================

			PdfPTable infoTable = new PdfPTable(2);

			infoTable.setWidthPercentage(100);

			infoTable.setWidths(new float[] { 50, 50 });

			// BILL TO
			PdfPCell billingCell = createInfoCell();

			billingCell.addElement(new Paragraph("BILL TO", SECTION_FONT));

			billingCell.addElement(new Paragraph(safe(order.getShippingFullName()), NORMAL_FONT));

			billingCell.addElement(new Paragraph("Mobile: " + safe(order.getShippingMobileNumber()), MUTED_FONT));

			// SHIPPING
			PdfPCell shippingCell = createInfoCell();

			shippingCell.addElement(new Paragraph("SHIPPING ADDRESS", SECTION_FONT));

			shippingCell.addElement(new Paragraph(safe(order.getShippingAddressLine1()), NORMAL_FONT));

			if (order.getShippingAddressLine2() != null && !order.getShippingAddressLine2().isBlank()) {

				shippingCell.addElement(new Paragraph(order.getShippingAddressLine2(), NORMAL_FONT));
			}

			shippingCell.addElement(
					new Paragraph(safe(order.getShippingCity()) + ", " + safe(order.getShippingState()), NORMAL_FONT));

			shippingCell.addElement(new Paragraph(
					safe(order.getShippingCountry()) + " - " + safe(order.getShippingPostalCode()), MUTED_FONT));

			infoTable.addCell(billingCell);
			infoTable.addCell(shippingCell);

			document.add(infoTable);

			document.add(new Paragraph(" "));

			// =================================================
			// ORDER INFORMATION
			// =================================================

			PdfPTable orderInfo = new PdfPTable(3);

			orderInfo.setWidthPercentage(100);

			orderInfo.setWidths(new float[] { 33, 33, 34 });

			orderInfo.addCell(createStatusCell("ORDER STATUS", String.valueOf(order.getOrderStatus())));

			orderInfo.addCell(createStatusCell("PAYMENT STATUS", String.valueOf(order.getPaymentStatus())));
			
			PaymentResponseDTO payment = paymentService.getPaymentByOrder(order.getId());

			orderInfo.addCell(createStatusCell("PAYMENT METHOD", getPaymentMethod(payment)));

			document.add(orderInfo);

			document.add(new Paragraph(" "));

			// =================================================
			// PRODUCT TABLE
			// =================================================

			PdfPTable table = new PdfPTable(5);

			table.setWidthPercentage(100);

			table.setWidths(new float[] { 35, 17, 16, 12, 20 });

			addHeaderCell(table, "PRODUCT");

			addHeaderCell(table, "SKU");

			addHeaderCell(table, "PRICE");

			addHeaderCell(table, "QTY");

			addHeaderCell(table, "SUBTOTAL");

			List<OrderItem> items = orderItemDAO.findByOrder(order);

			boolean alternate = false;

			for (OrderItem item : items) {

				java.awt.Color rowColor = alternate ? LIGHT : WHITE;

				addProductCell(table, item.getProductVariant().getProduct().getName(), rowColor, Element.ALIGN_LEFT);

				addProductCell(table, item.getProductVariant().getSku(), rowColor, Element.ALIGN_LEFT);

				addProductCell(table, "Rs. " + formatAmount(item.getPriceAtPurchase()), rowColor, Element.ALIGN_RIGHT);

				addProductCell(table, String.valueOf(item.getQuantity()), rowColor, Element.ALIGN_CENTER);

				addProductCell(table, "Rs. " + formatAmount(item.getSubtotal()), rowColor, Element.ALIGN_RIGHT);

				alternate = !alternate;
			}

			document.add(table);

			document.add(new Paragraph(" "));

			// =================================================
			// TOTAL SECTION
			// =================================================

			PdfPTable totalTable = new PdfPTable(2);

			totalTable.setWidthPercentage(100);

			totalTable.setWidths(new float[] { 65, 35 });

			PdfPCell noteCell = new PdfPCell();

			noteCell.setBorder(Rectangle.NO_BORDER);

			Paragraph note = new Paragraph("Thank you for choosing ShopVerse.", SMALL_BOLD_FONT);

			noteCell.addElement(note);

			Paragraph support = new Paragraph("We appreciate your business.", MUTED_FONT);

			noteCell.addElement(support);

			totalTable.addCell(noteCell);

			PdfPCell totalCell = new PdfPCell();

			totalCell.setBorder(Rectangle.NO_BORDER);

			totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

			Paragraph totalLabel = new Paragraph("TOTAL AMOUNT", SMALL_BOLD_FONT);

			totalLabel.setAlignment(Element.ALIGN_RIGHT);

			totalCell.addElement(totalLabel);

			Paragraph total = new Paragraph("Rs. " + formatAmount(order.getTotalAmount()), TOTAL_FONT);

			total.setAlignment(Element.ALIGN_RIGHT);

			totalCell.addElement(total);

			totalTable.addCell(totalCell);

			document.add(totalTable);

			document.add(new Paragraph(" "));

			// =================================================
			// FOOTER
			// =================================================

			PdfPTable footer = new PdfPTable(1);

			footer.setWidthPercentage(100);

			PdfPCell footerCell = new PdfPCell();

			footerCell.setBackgroundColor(DARK);

			footerCell.setPadding(12);

			footerCell.setBorder(Rectangle.NO_BORDER);

			Paragraph footerTitle = new Paragraph("SHOPVERSE", new Font(Font.HELVETICA, 10, Font.BOLD, WHITE));

			footerTitle.setAlignment(Element.ALIGN_CENTER);

			footerCell.addElement(footerTitle);

			Paragraph footerText = new Paragraph(
					"This is a computer-generated invoice and does not require a signature.",
					new Font(Font.HELVETICA, 7, Font.NORMAL, new java.awt.Color(203, 213, 225)));

			footerText.setAlignment(Element.ALIGN_CENTER);

			footerCell.addElement(footerText);

			footer.addCell(footerCell);

			document.add(footer);

			document.close();

			return outputStream.toByteArray();

		} catch (Exception e) {

			e.printStackTrace();

			return null;
		}
	}

	// =========================================================
	// HELPERS
	// =========================================================

	private PdfPCell createInfoCell() {

		PdfPCell cell = new PdfPCell();

		cell.setBackgroundColor(LIGHT);

		cell.setBorderColor(BORDER);

		cell.setPadding(12);

		return cell;
	}

	private PdfPCell createStatusCell(String label, String value) {

		PdfPCell cell = new PdfPCell();

		cell.setBackgroundColor(LIGHT);

		cell.setBorderColor(BORDER);

		cell.setPadding(9);

		Paragraph labelParagraph = new Paragraph(label, MUTED_FONT);

		cell.addElement(labelParagraph);

		Paragraph valueParagraph = new Paragraph(value, SMALL_BOLD_FONT);

		cell.addElement(valueParagraph);

		return cell;
	}

	private void addHeaderCell(PdfPTable table, String text) {

		PdfPCell cell = new PdfPCell(new Phrase(text, TABLE_HEADER_FONT));

		cell.setBackgroundColor(DARK);

		cell.setPadding(8);

		cell.setHorizontalAlignment(Element.ALIGN_CENTER);

		cell.setBorderColor(DARK);

		table.addCell(cell);
	}

	private void addProductCell(PdfPTable table, String text, java.awt.Color background, int alignment) {

		PdfPCell cell = new PdfPCell(new Phrase(safe(text), NORMAL_FONT));

		cell.setBackgroundColor(background);

		cell.setPadding(8);

		cell.setHorizontalAlignment(alignment);

		cell.setBorderColor(BORDER);

		table.addCell(cell);
	}

	private String formatAmount(Object amount) {

		if (amount == null)
			return "0.00";

		try {

			java.math.BigDecimal value = new java.math.BigDecimal(amount.toString());

			return value.setScale(2, java.math.RoundingMode.HALF_UP).toString();

		} catch (Exception e) {

			return amount.toString();
		}
	}

	private String safe(String value) {

		return value == null ? "" : value;
	}

	private String getPaymentMethod(PaymentResponseDTO payment) {

		if (payment.getPaymentMethod() == null)
			return "N/A";

		return payment.getPaymentMethod().toString();
	}
}