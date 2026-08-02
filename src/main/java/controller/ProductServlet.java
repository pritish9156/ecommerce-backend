package controller;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import entity.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ProductService;
import dto.request.ProductRequestDTO;
import dto.request.ProductSearchRequestDTO;
import dto.response.ApiResponse;
import dto.response.ProductCardResponseDTO;
import dto.response.ProductDetailsResponseDTO;
import dto.response.ProductSearchResponseDTO;

@WebServlet("/product/*")
public class ProductServlet extends HttpServlet {

	private ProductService productService;
	private ObjectMapper objectMapper;

	@Override
	public void init() {

		productService = new ProductService();

		objectMapper = new ObjectMapper();

		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String pathInfo = request.getPathInfo();

		if (pathInfo != null && pathInfo.equals("/search")) {

			ProductSearchRequestDTO dto = objectMapper.readValue(request.getInputStream(),
					ProductSearchRequestDTO.class);

			ProductSearchResponseDTO responseDto = productService.searchProducts(dto);

			response.setContentType("application/json");

			objectMapper.writeValue(response.getWriter(), responseDto);

			return;
		}

		ProductRequestDTO dto = objectMapper.readValue(request.getInputStream(), ProductRequestDTO.class);

		ApiResponse apiResponse = productService.addProduct(dto);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), apiResponse);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String pathInfo = req.getPathInfo();

		if (pathInfo != null && pathInfo.startsWith("/details/")) {

			Long productId = Long.parseLong(pathInfo.replace("/details/", ""));

			ProductDetailsResponseDTO responseDto = productService.getProductDetails(productId);

			resp.setContentType("application/json");

			objectMapper.writeValue(resp.getWriter(), responseDto);

			return;
		}

		if (pathInfo != null && pathInfo.startsWith("/related-products/")) {

			Long productId = Long.parseLong(pathInfo.replace("/related-products/", ""));

			ApiResponse response = productService.getRelatedProducts(productId);

			resp.setContentType("application/json");

			objectMapper.writeValue(resp.getWriter(), response);

			return;
		}

		if (pathInfo != null && !pathInfo.equals("/")) {

			Long productId = Long.parseLong(pathInfo.substring(1));

			ProductDetailsResponseDTO dto = productService.getProductDetails(productId);

			resp.setContentType("application/json");

			objectMapper.writeValue(resp.getWriter(), dto);

			return;
		}

		List<ProductCardResponseDTO> products = productService.getAllProducts();

		resp.setContentType("application/json");

		objectMapper.writeValue(resp.getWriter(), products);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		ProductRequestDTO dto = objectMapper.readValue(req.getInputStream(), ProductRequestDTO.class);

		ApiResponse apiResponse = productService.updateProduct(dto);

		resp.setContentType("application/json");

		objectMapper.writeValue(resp.getWriter(), apiResponse);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String pathInfo = req.getPathInfo();

		if (pathInfo == null || pathInfo.equals("/")) {

			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product ID is required");

			return;
		}

		Long productId = Long.parseLong(pathInfo.substring(1));

		ApiResponse apiResponse = productService.deactivateProduct(productId);

		resp.setContentType("application/json");

		objectMapper.writeValue(resp.getWriter(), apiResponse);
	}
}