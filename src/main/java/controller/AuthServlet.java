package controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dto.request.LoginRequest;
import dto.request.RegisterRequest;
import dto.request.ResendVerificationRequest;
import dto.response.ApiResponse;
import dto.response.AuthResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AuthService;

@WebServlet("/auth/*")
public class AuthServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private AuthService authService;
	private ObjectMapper objectMapper;

	@Override
	public void init() {

		authService = new AuthService();

		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String path = request.getPathInfo();

		if ("/verify".equals(path)) {

			String token = request.getParameter("token");

			ApiResponse apiResponse = authService.verifyEmail(token);

			String message = URLEncoder.encode(apiResponse.getMessage(), StandardCharsets.UTF_8);

			String status = apiResponse.isSuccess() ? "success" : "error";

			response.sendRedirect("http://localhost:5173/login?verification=" + status + "&message=" + message);

			return;
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String path = request.getPathInfo();

		switch (path) {

		case "/register":
			register(request, response);
			break;

		case "/login":
			login(request, response);
			break;

		case "/resend-verification":
			resendVerification(request, response);
			break;

		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void register(HttpServletRequest request, HttpServletResponse response) throws IOException {

		RegisterRequest registerRequest = objectMapper.readValue(request.getInputStream(), RegisterRequest.class);

		ApiResponse apiResponse = authService.register(registerRequest);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), apiResponse);
	}

	private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {

		LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

		AuthResponse authResponse = authService.login(loginRequest);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), authResponse);
	}

	private void resendVerification(HttpServletRequest request, HttpServletResponse response) throws IOException {

		ResendVerificationRequest resendRequest = objectMapper.readValue(request.getInputStream(),
				ResendVerificationRequest.class);

		ApiResponse apiResponse = authService.resendVerificationEmail(resendRequest.getEmail());

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), apiResponse);
	}
}