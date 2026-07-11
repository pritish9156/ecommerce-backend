package controller;

import java.io.IOException;
import java.io.PrintWriter;

import com.fasterxml.jackson.databind.ObjectMapper;

import dto.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/health-check/up")
public class HealthCheckServlet extends HttpServlet{
	
	ObjectMapper objectMapper;
	
	@Override
	public void init() throws ServletException {
		objectMapper = new ObjectMapper();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		ApiResponse response = new ApiResponse(true, "Server is Up and Running..");
		
		resp.setContentType("application/json");
		
		objectMapper.writeValue(resp.getOutputStream(), response);
	}
}
