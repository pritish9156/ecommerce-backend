package service;

import dao.OrderDAO;
import dao.PaymentDAO;
import dto.UpdatePaymentStatusDTO;
import dto.response.ApiResponse;
import dto.response.PaymentResponseDTO;
import dto.response.RazorpayOrderResponseDTO;
import entity.Order;
import entity.Payment;
import entity.enums.OrderStatus;
import entity.enums.PaymentMethod;
import entity.enums.PaymentStatus;

public class PaymentService {

	private PaymentDAO paymentDAO;
	private OrderDAO orderDAO;
	private OrderService orderService;

	public PaymentService() {

		paymentDAO = new PaymentDAO();
		orderDAO = new OrderDAO();
		orderService = new OrderService();
	}

	public PaymentResponseDTO getPaymentByOrder(Long orderId) {

		Payment payment = paymentDAO.findByOrderId(orderId);

		if (payment == null)
			return null;

		PaymentResponseDTO dto = new PaymentResponseDTO();

		dto.setPaymentId(payment.getId());

		dto.setTransactionId(payment.getTransactionId());

		dto.setPaymentMethod(payment.getPaymentMethod());

		dto.setPaymentStatus(payment.getPaymentStatus());

		return dto;
	}

	public ApiResponse updatePaymentStatus(UpdatePaymentStatusDTO dto) {

		Payment payment = paymentDAO.findById(dto.getPaymentId());

		if (payment == null)
			return new ApiResponse(false, "Payment not found");

		Order order = payment.getOrder();

		payment.setPaymentStatus(dto.getPaymentStatus());
		order.setPaymentStatus(dto.getPaymentStatus());

		boolean paymentStatus = paymentDAO.update(payment);
		boolean orderPaymentStatus = orderDAO.update(order);

		if (paymentStatus && orderPaymentStatus)
			return new ApiResponse(true, "Payment status Updated");
		else
			return new ApiResponse(false, "Failed to update payment status");
	}

	public ApiResponse retryPayment(Long orderId) {

		Order order = orderDAO.findById(orderId);

		if (order == null)
			return new ApiResponse(false, "Order not found.");

		if (order.getOrderStatus() == OrderStatus.CANCELLED)
			return new ApiResponse(false, "Cannot retry payment for a cancelled order.");

		if (order.getOrderStatus() == OrderStatus.DELIVERED)
			return new ApiResponse(false, "Cannot retry payment after the order has been delivered.");

		Payment payment = paymentDAO.findByOrder(order);

		if (payment == null)
			return new ApiResponse(false, "Payment record not found.");

		if (payment.getPaymentStatus() == PaymentStatus.SUCCESS)
			return new ApiResponse(false, "Payment has already been completed for this order.");

		RazorpayOrderResponseDTO dto = orderService.createRazorpayOrder(orderId);

		if (dto == null)
			return new ApiResponse(false, "Unable to initiate payment retry. Please try again.");

		return new ApiResponse(true, "Payment retry initiated successfully.", dto);
	}

	public ApiResponse payCodOrder(Long orderId) {

		Order order = orderDAO.findById(orderId);

		if (order == null)
			return new ApiResponse(false, "order not found");

		if (order.getOrderStatus() == OrderStatus.CANCELLED)
			return new ApiResponse(false, "order is cancelled");

		if (order.getOrderStatus() == OrderStatus.DELIVERED)
			return new ApiResponse(false, "order is already delivered");

		Payment payment = paymentDAO.findByOrder(order);

		if (payment == null)
			return new ApiResponse(false, "Payment record not found");

		if (payment.getPaymentStatus() == PaymentStatus.SUCCESS)
			return new ApiResponse(false, "payment is already paid for this order");

		if (payment.getPaymentMethod() != PaymentMethod.COD)
			return new ApiResponse(false, "payment method must be cash on delivery");

		if (payment.getPaymentStatus() != PaymentStatus.PENDING)
			return new ApiResponse(false, "payment status should be pending");

		RazorpayOrderResponseDTO dto = orderService.createRazorpayOrder(orderId);

		if (dto == null)
			return new ApiResponse(false, "Unable to initiate online payment.");

		return new ApiResponse(true, "Online payment initiated successfully.", dto);
	}
}