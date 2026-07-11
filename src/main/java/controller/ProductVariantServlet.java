package controller;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dto.ApiResponse;
import dto.ProductVariantRequestDTO;
import entity.ProductVariant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ProductVariantService;

@WebServlet("/variant/*")
public class ProductVariantServlet extends HttpServlet {

	private ProductVariantService productVariantService;
	private ObjectMapper objectMapper;

	@Override
	public void init() {

		productVariantService = new ProductVariantService();

		objectMapper = new ObjectMapper();

		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		ProductVariantRequestDTO dto = objectMapper.readValue(req.getInputStream(), ProductVariantRequestDTO.class);

		ApiResponse response = productVariantService.addProductVariant(dto);

		resp.setContentType("application/json");

		objectMapper.writeValue(resp.getWriter(), response);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String pathInfo = req.getPathInfo();

		if (pathInfo != null && !pathInfo.equals("/")) {

			Long id = Long.parseLong(pathInfo.substring(1));

			ProductVariant variant = productVariantService.getVariant(id);

			resp.setContentType("application/json");

			objectMapper.writeValue(resp.getWriter(), variant);

			return;
		}

		List<ProductVariant> variants = productVariantService.getAllVariants();

		resp.setContentType("application/json");

		objectMapper.writeValue(resp.getWriter(), variants);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		ProductVariantRequestDTO dto = objectMapper.readValue(req.getInputStream(), ProductVariantRequestDTO.class);

		ApiResponse response = productVariantService.updateProductVariant(dto);

		resp.setContentType("application/json");

		objectMapper.writeValue(resp.getWriter(), response);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String pathInfo = req.getPathInfo();

		if (pathInfo == null || pathInfo.equals("/")) {

			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Variant ID is required");

			return;
		}

		Long variantId = Long.parseLong(pathInfo.substring(1));

		ApiResponse response = productVariantService.deactivateVariant(variantId);

		resp.setContentType("application/json");

		objectMapper.writeValue(resp.getWriter(), response);
	}
}