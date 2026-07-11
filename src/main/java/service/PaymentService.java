package service;

import dao.PaymentDAO;
import dto.ApiResponse;
import dto.PaymentResponseDTO;
import dto.UpdatePaymentStatusDTO;
import entity.Payment;

public class PaymentService {

	private PaymentDAO paymentDAO;

	public PaymentService() {

		paymentDAO = new PaymentDAO();
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

		payment.setPaymentStatus(dto.getPaymentStatus());

		paymentDAO.update(payment);

		return new ApiResponse(true, "Payment Updated");
	}
}