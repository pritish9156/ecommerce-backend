package controller;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dto.ApiResponse;
import dto.CategoryRequestDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CategoryService;

@WebServlet("/category/*")
public class CategoryServlet extends HttpServlet {
	
	CategoryService categoryService;
	ObjectMapper objectMapper;

	public void init() {
		
		categoryService = new CategoryService();
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		CategoryRequestDTO dto = objectMapper.readValue(req.getInputStream(), CategoryRequestDTO.class);
		
		ApiResponse apiResponse = categoryService.createCategory(dto);
		
		resp.setContentType("application/json");
		
		objectMapper.writeValue(resp.getWriter(), apiResponse);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		ApiResponse apiResponse = categoryService.getAllCategories();
		
		resp.setContentType("application/json");
		
		objectMapper.writeValue(resp.getWriter(), apiResponse);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		CategoryRequestDTO dto = objectMapper.readValue(req.getInputStream(), CategoryRequestDTO.class);
		
		ApiResponse apiResponse = categoryService.updateCategory(dto);
		
		resp.setContentType("application/json");
		
		objectMapper.writeValue(resp.getWriter(), apiResponse);
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String pathInfo = req.getPathInfo();
		
		if(pathInfo == null || pathInfo.equals("/")) {
			
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Category Id is required");
			
		}
		
		Long categoryId = Long.parseLong(pathInfo.substring(1));
		
		ApiResponse apiResponse = categoryService.deactivateCategory(categoryId);
		
		resp.setContentType("application/json");
		
		objectMapper.writeValue(resp.getWriter(), apiResponse);
	}
	
}
