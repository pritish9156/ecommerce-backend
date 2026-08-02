package controller;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dao.ProductDAO;
import dao.ReviewDAO;
import dto.request.ReviewRequestDTO;
import dto.response.ApiResponse;
import entity.Product;
import entity.Review;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ReviewService;

@WebServlet("/review/*")
public class ReviewServlet extends HttpServlet {

	private ReviewService reviewService;
	private ReviewDAO reviewDAO;
	private ProductDAO productDAO;
	private ObjectMapper objectMapper;

	@Override
	public void init() {

		reviewService = new ReviewService();

		reviewDAO = new ReviewDAO();

		productDAO = new ProductDAO();

		objectMapper = new ObjectMapper();

		objectMapper.registerModule(new JavaTimeModule());

		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = (String) request.getAttribute("email");

		ReviewRequestDTO dto = objectMapper.readValue(request.getInputStream(), ReviewRequestDTO.class);

		ApiResponse apiResponse = reviewService.addReview(dto, email);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), apiResponse);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String path = request.getPathInfo();

		if (path == null || path.equals("/")) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST);

			return;
		}

		Long productId = Long.parseLong(path.substring(1));

		Product product = productDAO.findById(productId);

		List<Review> reviews = reviewDAO.findByProduct(product);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), reviews);
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = (String) request.getAttribute("email");

		ReviewRequestDTO dto = objectMapper.readValue(request.getInputStream(), ReviewRequestDTO.class);

		ApiResponse apiResponse = reviewService.updateReview(dto, email);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), apiResponse);
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = (String) request.getAttribute("email");

		Long reviewId = Long.parseLong(request.getPathInfo().substring(1));

		ApiResponse apiResponse = reviewService.deleteReview(reviewId, email);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), apiResponse);
	}
}