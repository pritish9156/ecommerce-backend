package controller;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dto.ApiResponse;
import dto.PlaceOrderRequestDTO;
import entity.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.OrderService;

@WebServlet("/orders/*")
public class OrderServlet extends HttpServlet {

	private OrderService orderService;
	private ObjectMapper objectMapper;

	@Override
	public void init() {

		orderService = new OrderService();

		objectMapper = new ObjectMapper();

		objectMapper.registerModule(new JavaTimeModule());

		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = (String) request.getAttribute("email");

		PlaceOrderRequestDTO dto = objectMapper.readValue(request.getInputStream(), PlaceOrderRequestDTO.class);

		ApiResponse apiResponse = orderService.placeOrder(dto, email);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), apiResponse);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = (String) request.getAttribute("email");

		List<Order> orders = orderService.getOrders(email);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), orders);
	}
}