package service;

import dao.OrderDAO;
import dao.PaymentDAO;
import dto.UpdatePaymentStatusDTO;
import dto.response.ApiResponse;
import dto.response.PaymentResponseDTO;
import dto.response.RazorpayOrderResponseDTO;
import entity.Order;
import entity.Payment;

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
		
		RazorpayOrderResponseDTO dto = orderService.createRazorpayOrder(orderId);
		
		if(dto == null)
			return new ApiResponse(false, "failed to retry your payment");
		else
			return new ApiResponse(true, "retry payment initiated", dto);
		
	}
}