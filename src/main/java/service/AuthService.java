package service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import auth.VerificationToken;
import dao.UserDAO;
import dao.VerificationTokenDAO;
import dto.request.LoginRequest;
import dto.request.RegisterRequest;
import dto.response.ApiResponse;
import dto.response.AuthResponse;
import entity.User;
import entity.enums.Role;
import entity.enums.TokenType;
import util.BCryptUtil;
import util.JwtUtil;

public class AuthService {
	
	private UserDAO userDAO;
	private VerificationTokenDAO verificationTokenDAO;
	
	public AuthService() {

	    userDAO = new UserDAO();

	    verificationTokenDAO =
	            new VerificationTokenDAO();
	}
	
	public ApiResponse register(RegisterRequest request) {
		
		User existingUser = userDAO.findByEmail(request.getEmail());
	
		if(existingUser != null) {
			return new ApiResponse(
					false,
					"Email already registered.");
		}
		
		User existingMobileUser = userDAO.findByMobileNumber(request.getMobileNumber());

		if(existingMobileUser != null) {

		    return new ApiResponse(
		            false,
		            "Mobile number already registered.");
		}
		
		String hashedPassword = BCryptUtil.hashPassword(request.getPassword());
		
		User user = new User();
		
		user.setFirstName(request.getFirstName());

		user.setLastName(request.getLastName());

		user.setEmail(request.getEmail());

		user.setMobileNumber(request.getMobileNumber());

		user.setPassword(hashedPassword);
		
		user.setRole(Role.CUSTOMER);
		
		boolean UserSaved = userDAO.save(user);

		if(!UserSaved) {
			return new ApiResponse(
			        true,
			        "Registration failed.");
		}
		
		String token = UUID.randomUUID().toString();
		
		VerificationToken verificationToken = new VerificationToken();
		
		verificationToken.setUser(user);

		verificationToken.setToken(token);

		verificationToken.setTokenType(TokenType.EMAIL_VERIFICATION);
		
		verificationToken.setExpiryTime(LocalDateTime.now().plusHours(24));
		
		boolean saved = verificationTokenDAO.save(verificationToken);
		
		if(!saved) {
		    return new ApiResponse(
		            false,
		            "Failed to generate verification token.");
		}
		
		EmailService.sendVerificationEmail(
		        user.getEmail(),
		        token
		);
		
		return new ApiResponse(
		        true,
		        "Registration successful. Please verify your email."
		);

	}
	
	
	public ApiResponse verifyEmail(String token) {
		
		VerificationToken verificationToken = verificationTokenDAO.findByToken(token);
		
		if(verificationToken == null) {

		    return new ApiResponse(
		            false,
		            "Invalid verification token.");
		}
		
		if(verificationToken.isUsed()) {

		    return new ApiResponse(
		            false,
		            "Token already used.");
		}
		
		if(
			    verificationToken
			        .getExpiryTime()
			        .isBefore(LocalDateTime.now())
			) {

			    return new ApiResponse(
			            false,
			            "Verification token expired.");
			}
		
		User user = verificationToken.getUser();
		
		user.setEmailVerified(true);
		
		boolean userUpdated = userDAO.update(user);
		
		if(!userUpdated) {
		    return new ApiResponse(
		            false,
		            "Failed to verify email.");
		}
		
		verificationToken.setUsed(true);

		verificationToken.setUsedAt(LocalDateTime.now());
		
		verificationTokenDAO.update(verificationToken);
		
		return new ApiResponse(
		        true,
		        "Email verified successfully.");
	}
	
	public ApiResponse resendVerificationEmail(String email) {
		
		User user = userDAO.findByEmail(email);
		
		if(user == null) {

		    return new ApiResponse(
		            false,
		            "User not found.");
		}
		
		if(user.isEmailVerified()) {

		    return new ApiResponse(
		            false,
		            "Email already verified.");
		}
		
		List<VerificationToken> tokens = verificationTokenDAO.findActiveVerificationTokens(user);
		
		LocalDateTime now = LocalDateTime.now();
		
		for(VerificationToken token : tokens) {	
			token.setUsed(true);
			token.setUsedAt(now);
			verificationTokenDAO.update(token);
		}
		
		String newToken = UUID.randomUUID().toString();
		
		VerificationToken verificationToken = new VerificationToken();
		
		verificationToken.setUser(user);

		verificationToken.setToken(newToken);

		verificationToken.setTokenType(TokenType.EMAIL_VERIFICATION);
		
		verificationToken.setExpiryTime(now.plusHours(24));
		
		boolean saved = verificationTokenDAO.save(verificationToken);
		
		if(!saved) {
		    return new ApiResponse(
		            false,
		            "Failed to generate verification token.");
		}
		
		EmailService.sendVerificationEmail(
		        user.getEmail(),
		        newToken
		);
		
		return new ApiResponse(
		        true,
		        "Verification email sent.");
		
	}
	
	
	public AuthResponse login(LoginRequest request) {
		
		User user = userDAO.findByEmail(request.getEmail());
		
		if(user == null) {
			return new AuthResponse(false, "Invalid email or password", null, null, null, null);
		}
		
		boolean passwordStatus = BCryptUtil.verifyPassword(request.getPassword(), user.getPassword());
		
		if(passwordStatus==false) {
			return new AuthResponse(false, "Invalid password", null, null, null, null);
		}
		
		if(!user.isEmailVerified()) {
			return new AuthResponse(false, "Please verify your email before logging in.", null, null, null, null);
		}
		
		if(!user.isActive()) {
			return new AuthResponse(false, "Account is disabled.", null, null, null, null);
		}
		
		String token = JwtUtil.generateToken(user);
		
		return new AuthResponse(
		        true,
		        "Login successful",
		        token,
		        user.getFirstName() + " " + user.getLastName(),
		        user.getRole().name(),
		        user.getId()
		);
	}

}
