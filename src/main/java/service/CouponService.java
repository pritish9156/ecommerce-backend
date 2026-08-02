package service;

import java.math.BigDecimal;
import java.util.List;

import dao.CartDAO;
import dao.CartItemDAO;
import dao.CouponDAO;
import dao.ProductVariantDAO;
import dao.UserDAO;
import dto.ApplyBuyNowCouponDTO;
import dto.CouponCalculationDTO;
import dto.request.CouponRequestDTO;
import dto.response.ApiResponse;
import entity.Cart;
import entity.CartItem;
import entity.Coupon;
import entity.ProductVariant;
import entity.User;
import entity.enums.DiscountType;

public class CouponService {

	private CouponDAO couponDAO;
	private UserDAO userDAO;
	private CartDAO cartDAO;
	private CartItemDAO cartItemDAO;
	private ProductVariantDAO productVariantDAO;

	public CouponService() {

		couponDAO = new CouponDAO();

		userDAO = new UserDAO();

		cartDAO = new CartDAO();

		cartItemDAO = new CartItemDAO();
		
		productVariantDAO = new ProductVariantDAO();

	}

	public ApiResponse addCoupon(CouponRequestDTO dto) {

		Coupon existing = couponDAO.findByCode(dto.getCode());

		if (existing != null)
			return new ApiResponse(false, "Coupon already exists");

		Coupon coupon = new Coupon();

		coupon.setCode(dto.getCode());

		coupon.setDescription(dto.getDescription());

		coupon.setDiscountType(dto.getDiscountType());

		coupon.setDiscountValue(dto.getDiscountValue());

		coupon.setMinimumOrderAmount(dto.getMinimumOrderAmount());

		coupon.setUsageLimit(dto.getUsageLimit());

		coupon.setStartDate(dto.getStartDate());

		coupon.setEndDate(dto.getEndDate());

		boolean status = couponDAO.save(coupon);

		return status ? new ApiResponse(true, "Coupon created") : new ApiResponse(false, "Failed");
	}

	public List<Coupon> getAllCoupons() {

		return couponDAO.findAll();
	}

	public ApiResponse updateCoupon(CouponRequestDTO dto) {

		Coupon coupon = couponDAO.findById(dto.getId());

		if (coupon == null)
			return new ApiResponse(false, "Coupon not found");

		coupon.setCode(dto.getCode());

		coupon.setDescription(dto.getDescription());

		coupon.setDiscountType(dto.getDiscountType());

		coupon.setDiscountValue(dto.getDiscountValue());

		coupon.setMinimumOrderAmount(dto.getMinimumOrderAmount());

		coupon.setUsageLimit(dto.getUsageLimit());

		coupon.setStartDate(dto.getStartDate());

		coupon.setEndDate(dto.getEndDate());

		boolean status = couponDAO.update(coupon);

		return status ? new ApiResponse(true, "Coupon updated") : new ApiResponse(false, "Update failed");
	}

	public ApiResponse deleteCoupon(Long couponId) {

		Coupon coupon = couponDAO.findById(couponId);

		if (coupon == null)
			return new ApiResponse(false, "Coupon not found");

		boolean status = couponDAO.delete(coupon);

		return status ? new ApiResponse(true, "Coupon deleted") : new ApiResponse(false, "Delete failed");
	}

	public CouponCalculationDTO calculateCoupon(String couponCode, String email) {

		User user = userDAO.findByEmail(email);

		if (user == null)
			throw new RuntimeException("User not found");

		Cart cart = cartDAO.findByUser(user);

		if (cart == null)
			throw new RuntimeException("Cart not found");

		List<CartItem> cartItems = cartItemDAO.findByCart(cart);

		if (cartItems.isEmpty())
			throw new RuntimeException("Cart is empty");

		BigDecimal total = BigDecimal.ZERO;

		for (CartItem item : cartItems) {

			BigDecimal subtotal = item.getProductVariant().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

			total = total.add(subtotal);
		}

		Coupon coupon = couponDAO.findByCode(couponCode);

		if (coupon == null)
			throw new RuntimeException("Invalid coupon");

		if (!coupon.isActive())
			throw new RuntimeException("Coupon inactive");

		if (coupon.getUsedCount() >= coupon.getUsageLimit())
			throw new RuntimeException("Coupon limit exceeded");

		if (coupon.getMinimumOrderAmount() != null && total.compareTo(coupon.getMinimumOrderAmount()) < 0) {

			throw new RuntimeException("Minimum order amount is ₹" + coupon.getMinimumOrderAmount());
		}

		BigDecimal discount = BigDecimal.ZERO;

		if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {

			discount = total.multiply(coupon.getDiscountValue()).divide(BigDecimal.valueOf(100));

		} else {

			discount = coupon.getDiscountValue();
		}

		if (discount.compareTo(total) > 0) {

			discount = total;
		}

		BigDecimal finalAmount = total.subtract(discount);

		CouponCalculationDTO dto = new CouponCalculationDTO();

		dto.setCouponCode(coupon.getCode());

		dto.setOriginalAmount(total);

		dto.setDiscountAmount(discount);

		dto.setFinalAmount(finalAmount);

		return dto;
	}

	public CouponCalculationDTO calculateBuyNowCoupon(ApplyBuyNowCouponDTO dto) {

		ProductVariant variant = productVariantDAO.findById(dto.getProductVariantId());

		if (variant == null)
			throw new RuntimeException("Variant not found");

		BigDecimal total = variant.getPrice().multiply(BigDecimal.valueOf(dto.getQuantity()));

		Coupon coupon = couponDAO.findByCode(dto.getCouponCode());

		if (coupon == null)
			throw new RuntimeException("Invalid coupon");

		if (!coupon.isActive())
			throw new RuntimeException("Coupon inactive");

		if (coupon.getUsedCount() >= coupon.getUsageLimit())
			throw new RuntimeException("Coupon limit exceeded");

		if (coupon.getMinimumOrderAmount() != null && total.compareTo(coupon.getMinimumOrderAmount()) < 0) {

			throw new RuntimeException("Minimum order amount is ₹" + coupon.getMinimumOrderAmount());
		}

		BigDecimal discount = BigDecimal.ZERO;

		if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {

			discount = total.multiply(coupon.getDiscountValue()).divide(BigDecimal.valueOf(100));

		} else {

			discount = coupon.getDiscountValue();
		}

		if (discount.compareTo(total) > 0) {

			discount = total;
		}

		BigDecimal finalAmount = total.subtract(discount);

		CouponCalculationDTO dto1 = new CouponCalculationDTO();

		dto1.setCouponCode(coupon.getCode());

		dto1.setOriginalAmount(total);

		dto1.setDiscountAmount(discount);

		dto1.setFinalAmount(finalAmount);

		return dto1;
	}
}