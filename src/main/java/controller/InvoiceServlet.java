package controller;

import java.io.IOException;

import dao.OrderDAO;
import entity.Order;
import entity.enums.OrderStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.InvoiceService;

@WebServlet("/invoice/*")
public class InvoiceServlet extends HttpServlet {

	private OrderDAO orderDAO;
	private InvoiceService invoiceService;

	@Override
	public void init() {

		orderDAO = new OrderDAO();

		invoiceService = new InvoiceService();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = (String) request.getAttribute("email");

		String pathInfo = request.getPathInfo();

		if (pathInfo == null || pathInfo.equals("/")) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Order ID is required.");

			return;
		}

		Long orderId = Long.parseLong(pathInfo.substring(1));

		Order order = orderDAO.findById(orderId);

		if (order == null) {

			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found.");

			return;
		}

		if (!order.getUser().getEmail().equals(email)) {

			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized.");

			return;
		}

		if (order.getOrderStatus() != OrderStatus.DELIVERED) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invoice is available only after delivery.");

			return;
		}

		byte[] invoice = invoiceService.generateInvoice(order);

		if (invoice == null) {

			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to generate invoice.");

			return;
		}

		response.setContentType("application/pdf");

		response.setHeader("Content-Disposition",
				"attachment; filename=\"Invoice-" + order.getOrderNumber() + ".pdf\"");

		response.setContentLength(invoice.length);

		response.getOutputStream().write(invoice);

		response.getOutputStream().flush();
	}
}