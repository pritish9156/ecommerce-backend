package controller;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dto.OrderDetailsDTO;
import dto.RazorpayFailedDTO;
import dto.RazorpaySuccessDTO;
import dto.UpdateOrderStatusDTO;
import dto.request.BuyNowRequestDTO;
import dto.request.PlaceOrderRequestDTO;
import dto.response.ApiResponse;
import dto.response.OrderResponseDTO;
import dto.response.RazorpayOrderResponseDTO;
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

		String path = request.getPathInfo();

		if (path != null && path.startsWith("/razorpay/")) {

			Long orderId = Long.parseLong(

					path.substring("/razorpay/".length()));

			RazorpayOrderResponseDTO dto = orderService.createRazorpayOrder(orderId);

			response.setContentType("application/json");

			objectMapper.writeValue(response.getWriter(), dto);

			return;
		}

		if ("/buy-now".equals(path)) {

			String email = (String) request.getAttribute("email");

			BuyNowRequestDTO dto = objectMapper.readValue(request.getInputStream(), BuyNowRequestDTO.class);

			ApiResponse responseObj = orderService.buyNow(dto, email);

			response.setContentType("application/json");

			objectMapper.writeValue(response.getWriter(), responseObj);

			return;
		}

		String email = (String) request.getAttribute("email");

		PlaceOrderRequestDTO dto = objectMapper.readValue(request.getInputStream(), PlaceOrderRequestDTO.class);

		ApiResponse apiResponse = orderService.placeOrder(dto, email);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), apiResponse);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String path = request.getPathInfo();

		if (path != null && path.startsWith("/admin-details/")) {

			Long orderId = Long.parseLong(

					path.substring("/admin-details/".length()));

			OrderDetailsDTO dto = orderService.getAdminOrderDetails(orderId);

			response.setContentType("application/json");

			objectMapper.writeValue(response.getWriter(), dto);

			return;
		}

		if (path != null && path.startsWith("/details/")) {

			Long orderId = Long.parseLong(path.substring("/details/".length()));

			String email = (String) request.getAttribute("email");

			OrderDetailsDTO dto = orderService.getOrderDetails(orderId, email);

			response.setContentType("application/json");

			objectMapper.writeValue(response.getWriter(), dto);

			return;
		}

		if ("/all".equals(path)) {

			List<OrderResponseDTO> orders = orderService.getAllOrders();

			response.setContentType("application/json");

			objectMapper.writeValue(response.getWriter(), orders);

			return;
		}

		String email = (String) request.getAttribute("email");

		if (path != null && !path.equals("/")) {

			Long orderId = Long.parseLong(path.substring(1));

			Object dto = orderService.getOrderDetails(orderId, email);

			response.setContentType("application/json");

			objectMapper.writeValue(response.getWriter(), dto);

			return;
		}

		List<OrderResponseDTO> orders = orderService.getOrders(email);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), orders);
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String path = request.getPathInfo();

		if ("/razorpay-failed".equals(path)) {

			RazorpayFailedDTO dto = objectMapper.readValue(request.getInputStream(), RazorpayFailedDTO.class);

			ApiResponse apiResponse = orderService.markPaymentFailed(dto.getOrderId());

			objectMapper.writeValue(response.getWriter(), apiResponse);

			return;
		}

		if ("/razorpay-success".equals(path)) {

			RazorpaySuccessDTO dto = objectMapper.readValue(request.getInputStream(), RazorpaySuccessDTO.class);

			ApiResponse apiResponse = orderService.markRazorpaySuccess(dto);

			objectMapper.writeValue(response.getWriter(), apiResponse);

			return;
		}

		if (path != null && path.startsWith("/cancel/")) {

			Long orderId = Long.parseLong(path.replace("/cancel/", ""));

			String email = (String) request.getAttribute("email");

			ApiResponse apiResponse = orderService.cancelOrder(orderId, email);

			response.setContentType("application/json");

			objectMapper.writeValue(response.getWriter(), apiResponse);

			return;
		}

		UpdateOrderStatusDTO dto = objectMapper.readValue(request.getInputStream(), UpdateOrderStatusDTO.class);

		ApiResponse apiResponse = orderService.updateOrderStatus(dto);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), apiResponse);
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = (String) request.getAttribute("email");

		String pathInfo = request.getPathInfo();

		if (pathInfo == null || pathInfo.equals("/")) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Order ID Required");

			return;
		}

		Long orderId = Long.parseLong(pathInfo.substring(1));

		ApiResponse apiResponse = orderService.cancelOrder(orderId, email);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), apiResponse);
	}
}