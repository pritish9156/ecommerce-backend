package controller;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dto.ApiResponse;
import dto.PaymentResponseDTO;
import dto.UpdatePaymentStatusDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.PaymentService;

@WebServlet("/payment/*")
public class PaymentServlet extends HttpServlet {

	private PaymentService paymentService;
	private ObjectMapper objectMapper;

	@Override
	public void init() {

		paymentService = new PaymentService();

		objectMapper = new ObjectMapper();

		objectMapper.registerModule(new JavaTimeModule());

		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String path = request.getPathInfo();

		if (path == null || path.equals("/")) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST);

			return;
		}

		Long orderId = Long.parseLong(path.substring(1));

		PaymentResponseDTO dto = paymentService.getPaymentByOrder(orderId);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), dto);
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {

		UpdatePaymentStatusDTO dto = objectMapper.readValue(request.getInputStream(), UpdatePaymentStatusDTO.class);

		ApiResponse apiResponse = paymentService.updatePaymentStatus(dto);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), apiResponse);
	}
}