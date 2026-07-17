package filter;

import java.io.IOException;

import dao.UserDAO;
import entity.User;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.JwtUtil;

@WebFilter({ "/address/*", "/cart/*", "/wishlist/*", "/orders/*", "/users/*", "/review/*", "/coupon/*", "/invoice/*",
		"/review-ai/*" })
//@WebFilter("/*")
public class JwtFilter implements Filter {

	UserDAO userDAO = new UserDAO();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		System.out.println("JWT FILTER HIT");

		HttpServletRequest httpRequest = (HttpServletRequest) request;

		HttpServletResponse httpResponse = (HttpServletResponse) response;

		// Allow anyone to VIEW product reviews
		if ("GET".equalsIgnoreCase(httpRequest.getMethod())
				&& httpRequest.getRequestURI().startsWith(httpRequest.getContextPath() + "/review/")) {

			chain.doFilter(request, response);

			return;
		}

		// Everything below requires login

		String authHeader = httpRequest.getHeader("Authorization");

		System.out.println("HEADER = " + authHeader);

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {

			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			return;
		}

		String token = authHeader.substring(7);

		if (!JwtUtil.validateToken(token)) {

			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			return;
		}

		String email = JwtUtil.extractEmail(token);

		User user = userDAO.findByEmail(email);

		httpRequest.setAttribute("email", email);

		httpRequest.setAttribute("user", user);

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}
}