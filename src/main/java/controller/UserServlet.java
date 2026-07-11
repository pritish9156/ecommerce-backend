package controller;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dto.ApiResponse;
import dto.UpdateUserStatusDTO;
import entity.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.UserService;

@WebServlet("/users/*")
public class UserServlet extends HttpServlet {

	private UserService userService;
	private ObjectMapper objectMapper;

	@Override
	public void init() {

		userService = new UserService();

		objectMapper = new ObjectMapper();

		objectMapper.registerModule(new JavaTimeModule());

		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String path = request.getPathInfo();

		/*
		 * ADMIN GET /users/all
		 */
		if ("/all".equals(path)) {

			List<User> users = userService.getAllUsers();

			response.setContentType("application/json");

			objectMapper.writeValue(response.getWriter(), users);

			return;
		}

		/*
		 * CUSTOMER GET /users/profile
		 */
		String email = (String) request.getAttribute("email");

		User user = userService.getProfile(email);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), user);
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {

		UpdateUserStatusDTO dto = objectMapper.readValue(request.getInputStream(), UpdateUserStatusDTO.class);

		ApiResponse apiResponse = userService.updateUserStatus(dto);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), apiResponse);
	}
}