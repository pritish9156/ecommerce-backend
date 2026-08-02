package controller;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import dto.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ProfileDataService;

@WebServlet("/profile-data/*")
public class UserProfileServlet extends HttpServlet {
	
	ObjectMapper objectMapper;
	ProfileDataService profileDataService;
	
	@Override
	public void init() {	
		profileDataService = new ProfileDataService();
		objectMapper = new ObjectMapper();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String email = (String) request.getAttribute("email");
		
		ApiResponse ProfileResponse = profileDataService.getProfileData(email);
		
		response.setContentType("application/json");
		
		objectMapper.writeValue(response.getOutputStream(), ProfileResponse);
		
	}

}
