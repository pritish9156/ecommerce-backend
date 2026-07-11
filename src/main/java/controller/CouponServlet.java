package controller;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dto.ApiResponse;
import dto.ApplyBuyNowCouponDTO;
import dto.ApplyCouponRequestDTO;
import dto.CouponCalculationDTO;
import dto.CouponRequestDTO;
import entity.Coupon;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CouponService;

@WebServlet("/coupon/*")
public class CouponServlet extends HttpServlet {

	private CouponService couponService;
	private ObjectMapper objectMapper;

	@Override
	public void init() {

		couponService = new CouponService();

		objectMapper = new ObjectMapper();

		objectMapper.registerModule(new JavaTimeModule());

		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String pathInfo = request.getPathInfo();

		response.setContentType("application/json");

		if ("/apply-buy-now".equals(pathInfo)) {

			ApplyBuyNowCouponDTO dto = objectMapper.readValue(request.getInputStream(), ApplyBuyNowCouponDTO.class);

			CouponCalculationDTO result = couponService.calculateBuyNowCoupon(dto);

			objectMapper.writeValue(response.getWriter(), result);

			return;
		}

		if ("/apply".equals(pathInfo)) {

			String email = (String) request.getAttribute("email");

			ApplyCouponRequestDTO dto = objectMapper.readValue(request.getInputStream(), ApplyCouponRequestDTO.class);

			CouponCalculationDTO result = couponService.calculateCoupon(dto.getCouponCode(), email);

			objectMapper.writeValue(response.getWriter(), result);

			return;
		}

		CouponRequestDTO dto = objectMapper.readValue(request.getInputStream(), CouponRequestDTO.class);

		ApiResponse apiResponse = couponService.addCoupon(dto);

		objectMapper.writeValue(response.getWriter(), apiResponse);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<Coupon> coupons = couponService.getAllCoupons();

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), coupons);
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String pathInfo = request.getPathInfo();

		if (pathInfo == null || pathInfo.equals("/")) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Coupon ID required");

			return;
		}

		Long couponId = Long.parseLong(pathInfo.substring(1));

		ApiResponse apiResponse = couponService.deleteCoupon(couponId);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), apiResponse);
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		CouponRequestDTO dto = objectMapper.readValue(request.getInputStream(), CouponRequestDTO.class);

		ApiResponse apiResponse = couponService.updateCoupon(dto);

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), apiResponse);
	}
}