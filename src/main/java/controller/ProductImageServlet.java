package controller;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dto.request.ProductImageRequestDTO;
import dto.response.ApiResponse;
import entity.ProductImage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ProductImageService;

@WebServlet("/product-image/*")
public class ProductImageServlet extends HttpServlet {

	private ProductImageService productImageService;
	private ObjectMapper objectMapper;

	@Override
	public void init() {

		productImageService = new ProductImageService();

		objectMapper = new ObjectMapper();

		objectMapper.registerModule(new JavaTimeModule());

		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ProductImageRequestDTO dto = objectMapper.readValue(request.getInputStream(), ProductImageRequestDTO.class);

		ApiResponse apiResponse = productImageService.addProductImage(dto);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), apiResponse);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String pathInfo = request.getPathInfo();

		if (pathInfo == null || pathInfo.equals("/")) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product ID required");

			return;
		}

		Long productId = Long.parseLong(pathInfo.substring(1));

		List<ProductImage> images = productImageService.getImagesByProduct(productId);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), images);
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ProductImageRequestDTO dto = objectMapper.readValue(request.getInputStream(), ProductImageRequestDTO.class);

		ApiResponse apiResponse = productImageService.updateProductImage(dto);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), apiResponse);
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String pathInfo = request.getPathInfo();

		if (pathInfo == null || pathInfo.equals("/")) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Image ID required");

			return;
		}

		Long imageId = Long.parseLong(pathInfo.substring(1));

		ApiResponse apiResponse = productImageService.deleteProductImage(imageId);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), apiResponse);
	}
}