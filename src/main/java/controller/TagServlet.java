package controller;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dto.ApiResponse;
import dto.CategoryRequestDTO;
import dto.TagRequestDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CategoryService;
import service.TagService;

@WebServlet("/tag/*")
public class TagServlet extends HttpServlet{

	TagService tagService;
	ObjectMapper objectMapper;

	public void init() {
		
		tagService = new TagService();
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		TagRequestDTO dto = objectMapper.readValue(req.getInputStream(), TagRequestDTO.class);
		
		ApiResponse apiResponse = tagService.createTag(dto);
		
		resp.setContentType("application/json");
		
		objectMapper.writeValue(resp.getWriter(), apiResponse);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		ApiResponse apiResponse = tagService.getAllTags();
		
		resp.setContentType("application/json");
		
		objectMapper.writeValue(resp.getWriter(), apiResponse);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		TagRequestDTO dto = objectMapper.readValue(req.getInputStream(), TagRequestDTO.class);
		
		ApiResponse apiResponse = tagService.updateTag(dto);
		
		resp.setContentType("application/json");
		
		objectMapper.writeValue(resp.getWriter(), apiResponse);
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String pathInfo = req.getPathInfo();
		
		if(pathInfo == null || pathInfo.equals("/")) {
			
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tag Id is required");
			
		}
		
		Long tagId = Long.parseLong(pathInfo.substring(1));
		
		ApiResponse apiResponse = tagService.deactivateTag(tagId);
		
		resp.setContentType("application/json");
		
		objectMapper.writeValue(resp.getWriter(), apiResponse);
	}
}
