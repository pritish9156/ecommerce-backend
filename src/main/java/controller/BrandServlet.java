package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.BrandService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dto.ApiResponse;
import dto.BrandRequestDTO;
import entity.Brand;

@WebServlet("/brand/*")
public class BrandServlet extends HttpServlet {
	
	BrandService brandService;
	ObjectMapper objectMapper;
	
	@Override
	public void init() {
		brandService = new BrandService();
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(
			    SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
			);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		BrandRequestDTO dto = objectMapper.readValue(req.getInputStream(), BrandRequestDTO.class);
		
		ApiResponse apiResponse = brandService.addBrandDetails(dto);
		
		resp.setContentType("application/json");
		
		objectMapper.writeValue(resp.getWriter(), apiResponse);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		List<Brand> brands = brandService.getAllBrandDetails();
		
		resp.setContentType("application/json");
		
		objectMapper.writeValue(resp.getWriter(), brands);
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		BrandRequestDTO dto = objectMapper.readValue(req.getInputStream(), BrandRequestDTO.class);
		
		ApiResponse apiResponse = brandService.updateBrandDetails(dto);
		
		resp.setContentType("application/json");
		
		objectMapper.writeValue(resp.getWriter(), apiResponse);
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String pathInfo = req.getPathInfo();

		if (pathInfo == null || pathInfo.equals("/")) {

			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Brand ID is required");

			return;
		}

		Long brandId = Long.parseLong(pathInfo.substring(1));
		
		ApiResponse apiResponse = brandService.deactivateBrand(brandId);
		
		resp.setContentType("application/json");
		
		objectMapper.writeValue(resp.getWriter(), apiResponse);
	}
}
