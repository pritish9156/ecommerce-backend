package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dto.UpdateProfileDTO;
import dto.UpdateProfileImageDTO;
import dto.UpdateUserStatusDTO;
import dto.response.ApiResponse;
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

	private static final String UPLOAD_ROOT = "E:" + File.separator + "ShopSphereUploads";

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

		User user = (User) userService.getProfile(email).getData();

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), user);
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String path = request.getPathInfo();

		response.setContentType("application/json");

		if ("/status".equals(path)) {

			UpdateUserStatusDTO dto = objectMapper.readValue(request.getInputStream(), UpdateUserStatusDTO.class);

			ApiResponse apiResponse = userService.updateUserStatus(dto);

			objectMapper.writeValue(response.getWriter(), apiResponse);

			return;

		}

		if ("/profile".equals(path)) {

			String userEmail = (String) request.getAttribute("email");

			UpdateProfileDTO dto = objectMapper.readValue(request.getInputStream(), UpdateProfileDTO.class);

			ApiResponse apiResponse = userService.updateProfile(userEmail, dto);

			objectMapper.writeValue(response.getWriter(), apiResponse);

			return;
		}

		if ("/profile/image".equals(path)) {

			String userEmail = (String) request.getAttribute("email");

			UpdateProfileImageDTO dto = objectMapper.readValue(request.getInputStream(), UpdateProfileImageDTO.class);

			ApiResponse apiResponse = userService.updateProfileImage(userEmail, dto);

			objectMapper.writeValue(response.getWriter(), apiResponse);

			return;
		}

		response.setStatus(HttpServletResponse.SC_NOT_FOUND);

		objectMapper.writeValue(response.getWriter(), new ApiResponse(false, "Invalid user endpoint."));

	}
}