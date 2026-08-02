package controller;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import dto.request.ReviewAIRequestDTO;
import dto.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ReviewAIService;

@WebServlet("/review-ai/improve")
public class ReviewAIServlet extends HttpServlet {

	private ObjectMapper objectMapper;
	private ReviewAIService reviewAIService;

	@Override
	public void init() {

		objectMapper = new ObjectMapper();

		reviewAIService = new ReviewAIService();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ReviewAIRequestDTO dto = objectMapper.readValue(request.getInputStream(), ReviewAIRequestDTO.class);

		ApiResponse apiResponse = reviewAIService.improveReview(dto);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), apiResponse);
	}
}