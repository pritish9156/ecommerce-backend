package controller;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dto.AddToCartRequestDTO;
import dto.ApiResponse;
import dto.UpdateCartRequestDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CartService;

@WebServlet("/cart/*")
public class CartServlet extends HttpServlet {

	private CartService cartService;
	private ObjectMapper objectMapper;

	@Override
	public void init() {

		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		cartService = new CartService();

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = (String) request.getAttribute("email");

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), cartService.getCart(email));
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = (String) request.getAttribute("email");

		AddToCartRequestDTO dto = objectMapper.readValue(request.getInputStream(), AddToCartRequestDTO.class);

		ApiResponse apiResponse = cartService.addToCart(dto, email);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), apiResponse);

	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = (String) request.getAttribute("email");

		String pathInfo = request.getPathInfo();

		if (pathInfo == null || pathInfo.equals("/")) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Cart Item ID is required");

			return;
		}

		Long cartItemId = Long.parseLong(pathInfo.substring(1));

		UpdateCartRequestDTO dto = objectMapper.readValue(request.getInputStream(), UpdateCartRequestDTO.class);

		ApiResponse apiResponse = cartService.updateQuantity(cartItemId, dto.getQuantity(), email);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), apiResponse);
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = (String) request.getAttribute("email");

		String pathInfo = request.getPathInfo();

		ApiResponse apiResponse;

		if ("/clear".equals(pathInfo)) {

			apiResponse = cartService.clearCart(email);

		} else {

			if (pathInfo == null || pathInfo.equals("/")) {

				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Cart Item ID is required");

				return;
			}

			Long cartItemId = Long.parseLong(pathInfo.substring(1));

			apiResponse = cartService.removeItem(cartItemId, email);
		}

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), apiResponse);
	}

}
