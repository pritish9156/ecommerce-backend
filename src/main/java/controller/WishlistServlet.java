package controller;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dto.ApiResponse;
import dto.WishlistRequestDTO;
import dto.WishlistResponseDTO;
import entity.Wishlist;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.WishlistService;

@WebServlet("/wishlist/*")
public class WishlistServlet extends HttpServlet {

	private WishlistService wishlistService;
	private ObjectMapper objectMapper;

	@Override
	public void init() {

		wishlistService = new WishlistService();

		objectMapper = new ObjectMapper();

		objectMapper.registerModule(new JavaTimeModule());

		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = (String) request.getAttribute("email");

		WishlistRequestDTO dto = objectMapper.readValue(request.getInputStream(), WishlistRequestDTO.class);

		ApiResponse apiResponse = wishlistService.addToWishlist(dto, email);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), apiResponse);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = (String) request.getAttribute("email");

		List<WishlistResponseDTO> wishlistItems = wishlistService.getWishlist(email);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), wishlistItems);
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = (String) request.getAttribute("email");

		String pathInfo = request.getPathInfo();

		if (pathInfo == null || pathInfo.equals("/")) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Wishlist ID required");

			return;
		}

		Long wishlistId = Long.parseLong(pathInfo.substring(1));

		ApiResponse apiResponse = wishlistService.removeFromWishlist(wishlistId, email);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), apiResponse);
	}
}