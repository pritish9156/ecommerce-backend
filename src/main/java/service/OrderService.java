package service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import dao.AddressDAO;
import dao.CartDAO;
import dao.CartItemDAO;
import dao.CouponDAO;
import dao.OrderDAO;
import dao.OrderItemDAO;
import dao.PaymentDAO;
import dao.ProductVariantDAO;
import dao.UserDAO;
import dto.ApiResponse;
import dto.BuyNowRequestDTO;
import dto.OrderDetailsDTO;
import dto.OrderItemResponseDTO;
import dto.PlaceOrderRequestDTO;
import dto.RazorpayOrderResponseDTO;
import dto.RazorpaySuccessDTO;
import dto.UpdateOrderStatusDTO;
import entity.Address;
import entity.Cart;
import entity.CartItem;
import entity.Order;
import entity.OrderItem;
import entity.Payment;
import entity.ProductVariant;
import entity.User;
import entity.enums.OrderStatus;
import entity.enums.PaymentMethod;
import entity.enums.PaymentStatus;
import util.RazorpayUtil;
import entity.Coupon;
import entity.enums.DiscountType;

public class OrderService {

	private UserDAO userDAO;
	private AddressDAO addressDAO;
	private CartDAO cartDAO;
	private CartItemDAO cartItemDAO;
	private OrderDAO orderDAO;
	private OrderItemDAO orderItemDAO;
	private ProductVariantDAO productVariantDAO;
	private PaymentDAO paymentDAO;
	private CouponDAO couponDAO;

	public OrderService() {

		userDAO = new UserDAO();
		addressDAO = new AddressDAO();
		cartDAO = new CartDAO();
		cartItemDAO = new CartItemDAO();
		orderDAO = new OrderDAO();
		orderItemDAO = new OrderItemDAO();
		productVariantDAO = new ProductVariantDAO();
		paymentDAO = new PaymentDAO();
		couponDAO = new CouponDAO();
	}

	public ApiResponse placeOrder(PlaceOrderRequestDTO dto, String email) {

		User user = userDAO.findByEmail(email);

		if (user == null)
			return new ApiResponse(false, "User not found.");

		Address address = addressDAO.findById(dto.getAddressId());

		if (address == null)
			return new ApiResponse(false, "Address not found.");

		if (!address.getUser().getId().equals(user.getId()))
			return new ApiResponse(false, "Unauthorized address.");

		Cart cart = cartDAO.findByUser(user);

		if (cart == null)
			return new ApiResponse(false, "Cart not found.");

		List<CartItem> cartItems = cartItemDAO.findByCart(cart);

		if (cartItems.isEmpty())
			return new ApiResponse(false, "Cart is empty.");

		BigDecimal totalAmount = BigDecimal.ZERO;

		for (CartItem cartItem : cartItems) {

			ProductVariant variant = cartItem.getProductVariant();

			if (!variant.isActive())
				return new ApiResponse(false, "Product unavailable.");

			if (variant.getStock() < cartItem.getQuantity())
				return new ApiResponse(false, "Insufficient stock for " + variant.getSku());

			BigDecimal subtotal = variant.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));

			totalAmount = totalAmount.add(subtotal);
		}

		Order order = new Order();

		order.setOrderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

		order.setUser(user);

		order.setShippingFullName(address.getFullName());

		order.setShippingMobileNumber(address.getMobileNumber());

		order.setShippingAddressLine1(address.getAddressLine1());

		order.setShippingAddressLine2(address.getAddressLine2());

		order.setShippingLandmark(address.getLandmark());

		order.setShippingCity(address.getCity());

		order.setShippingState(address.getState());

		order.setShippingCountry(address.getCountry());

		order.setShippingPostalCode(address.getPostalCode());

		BigDecimal discount = BigDecimal.ZERO;

		if (dto.getCouponCode() != null && !dto.getCouponCode().isBlank()) {

			Coupon coupon = couponDAO.findByCode(dto.getCouponCode());

			if (coupon == null)
				return new ApiResponse(false, "Invalid Coupon");

			if (!coupon.isActive())
				return new ApiResponse(false, "Coupon inactive");

			if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {

				discount = totalAmount.multiply(coupon.getDiscountValue()).divide(BigDecimal.valueOf(100));

			} else {

				discount = coupon.getDiscountValue();
			}

			if (discount.compareTo(totalAmount) > 0) {

				discount = totalAmount;
			}

			coupon.setUsedCount(coupon.getUsedCount() + 1);

			couponDAO.update(coupon);
		}

		BigDecimal finalAmount = totalAmount.subtract(discount);

		order.setTotalAmount(finalAmount);

		boolean orderSaved = orderDAO.save(order);

		if (!orderSaved)
			return new ApiResponse(false, "Unable to place order.");

		Payment payment = new Payment();

		payment.setOrder(order);

		payment.setAmount(finalAmount);

		payment.setTransactionId(

				dto.getPaymentMethod()

						+ "-"

						+ System.currentTimeMillis());

		payment.setPaymentMethod(PaymentMethod.valueOf(dto.getPaymentMethod()));

		payment.setPaymentStatus(PaymentStatus.PENDING);

		paymentDAO.save(payment);

		for (CartItem cartItem : cartItems) {

			ProductVariant variant = cartItem.getProductVariant();

			BigDecimal subtotal = variant.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));

			OrderItem orderItem = new OrderItem();

			orderItem.setOrder(order);

			orderItem.setProductVariant(variant);

			orderItem.setQuantity(cartItem.getQuantity());

			orderItem.setPriceAtPurchase(variant.getPrice());

			orderItem.setSubtotal(subtotal);

			orderItemDAO.save(orderItem);

			variant.setStock(variant.getStock() - cartItem.getQuantity());

			productVariantDAO.update(variant);
		}

		cartItemDAO.deleteAllByCart(cart);

		PaymentMethod paymentMethod = PaymentMethod.valueOf(dto.getPaymentMethod());

		if (paymentMethod == PaymentMethod.COD)
			EmailService.sendOrderConfirmationEmail(order);

		Map<String, Object> data = new HashMap<>();

		data.put("orderId", order.getId());

		data.put("orderNumber", order.getOrderNumber());

		return new ApiResponse(

				true,

				"Order placed successfully",

				data);
	}

	public List<Order> getOrders(String email) {

		User user = userDAO.findByEmail(email);

		if (user == null)
			return List.of();

		return orderDAO.findByUser(user);
	}

	public List<Order> getAllOrders() {

		return orderDAO.findAll();
	}

	private int getOrderStatusLevel(OrderStatus status) {

		switch (status) {

		case PENDING:
			return 1;

		case CONFIRMED:
			return 2;

		case PROCESSING:
			return 3;

		case SHIPPED:
			return 4;

		case OUT_FOR_DELIVERY:
			return 5;

		case DELIVERED:
			return 6;

		default:
			return 0;
		}
	}

	public ApiResponse updateOrderStatus(UpdateOrderStatusDTO dto) {

		Order order = orderDAO.findById(dto.getOrderId());

		if (order == null)
			return new ApiResponse(false, "Order not found");

		if (order.getOrderStatus() == OrderStatus.CANCELLED) {

			return new ApiResponse(false, "Cancelled orders cannot be updated");
		}

		if (order.getOrderStatus() == OrderStatus.DELIVERED) {

			return new ApiResponse(false, "Delivered order cannot be modified");
		}

		OrderStatus newStatus = OrderStatus.valueOf(dto.getOrderStatus());

		if (getOrderStatusLevel(newStatus) < getOrderStatusLevel(order.getOrderStatus())) {

			return new ApiResponse(false, "Cannot move order backwards");
		}

		order.setOrderStatus(

				OrderStatus.valueOf(dto.getOrderStatus()));

		boolean updated = orderDAO.update(order);

		if (updated) {

			EmailService.sendOrderStatusUpdateEmail(order);

			if (newStatus == OrderStatus.DELIVERED) {

				InvoiceService invoiceService = new InvoiceService();

				byte[] invoicePdf = invoiceService.generateInvoice(order);

				if (invoicePdf != null) {

					EmailService.sendInvoiceEmail(order, invoicePdf);
				}
			}

			return new ApiResponse(true, "Order status updated successfully.");
		}

		return new ApiResponse(false, "Failed to update order status.");
	}

	public OrderDetailsDTO getOrderDetails(Long orderId, String email) {

		User user = userDAO.findByEmail(email);

		Order order = orderDAO.findById(orderId);

		if (order == null)
			return null;

		if (!order.getUser().getId().equals(user.getId()))
			return null;

		List<OrderItem> items = orderItemDAO.findByOrder(order);

		List<OrderItemResponseDTO> itemDTOs = new ArrayList<>();

		for (OrderItem item : items) {

			OrderItemResponseDTO itemDTO = new OrderItemResponseDTO();

			ProductVariant variant = item.getProductVariant();

			itemDTO.setId(item.getId());

			itemDTO.setProductVariantId(variant.getId());

			itemDTO.setSku(variant.getSku());

			itemDTO.setProductId(variant.getProduct().getId());

			itemDTO.setProductName(variant.getProduct().getName());

			itemDTO.setQuantity(item.getQuantity());

			itemDTO.setPriceAtPurchase(item.getPriceAtPurchase());

			itemDTO.setSubtotal(item.getSubtotal());

			itemDTOs.add(itemDTO);
		}

		OrderDetailsDTO dto = new OrderDetailsDTO();

		dto.setOrder(order);
		dto.setItems(itemDTOs);

		return dto;
	}

	public ApiResponse cancelOrder(Long orderId, String email) {

		User user = userDAO.findByEmail(email);

		Order order = orderDAO.findById(orderId);

		if (order == null)
			return new ApiResponse(false, "Order Not Found");

		if (!order.getUser().getId().equals(user.getId()))
			return new ApiResponse(false, "Unauthorized");

		if (order.getOrderStatus() != OrderStatus.PENDING

				&&

				order.getOrderStatus() != OrderStatus.CONFIRMED) {

			return new ApiResponse(false, "Cannot Cancel Order");
		}

		order.setOrderStatus(OrderStatus.CANCELLED);

		boolean updated = orderDAO.update(order);

		if (updated) {

			EmailService.sendOrderCancelledEmail(order);

			return new ApiResponse(true, "Order cancelled successfully.");
		}

		return new ApiResponse(false, "Unable to cancel order.");
	}

	public ApiResponse buyNow(BuyNowRequestDTO dto, String email) {

		User user = userDAO.findByEmail(email);

		if (user == null)
			return new ApiResponse(false, "User not found");

		ProductVariant variant = productVariantDAO.findById(dto.getProductVariantId());

		if (variant == null)
			return new ApiResponse(false, "Variant not found");

		Address address = addressDAO.findById(dto.getAddressId());

		if (address == null)
			return new ApiResponse(false, "Address not found");

		if (variant.getStock() < dto.getQuantity()) {

			return new ApiResponse(false, "Insufficient stock");
		}

		BigDecimal totalAmount = variant.getPrice().multiply(BigDecimal.valueOf(dto.getQuantity()));

		BigDecimal discount = BigDecimal.ZERO;

		if (dto.getCouponCode() != null && !dto.getCouponCode().isBlank()) {

			Coupon coupon = couponDAO.findByCode(dto.getCouponCode());

			if (coupon == null)
				return new ApiResponse(false, "Invalid Coupon");

			if (!coupon.isActive())
				return new ApiResponse(false, "Coupon inactive");

			if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {

				discount = totalAmount.multiply(coupon.getDiscountValue()).divide(BigDecimal.valueOf(100));

			} else {

				discount = coupon.getDiscountValue();
			}

			if (discount.compareTo(totalAmount) > 0) {

				discount = totalAmount;
			}

			coupon.setUsedCount(coupon.getUsedCount() + 1);

			couponDAO.update(coupon);
		}

		BigDecimal finalAmount = totalAmount.subtract(discount);

		Order order = new Order();

		order.setOrderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

		order.setUser(user);

		order.setShippingFullName(address.getFullName());

		order.setShippingMobileNumber(address.getMobileNumber());

		order.setShippingAddressLine1(address.getAddressLine1());

		order.setShippingAddressLine2(address.getAddressLine2());

		order.setShippingLandmark(address.getLandmark());

		order.setShippingCity(address.getCity());

		order.setShippingState(address.getState());

		order.setShippingCountry(address.getCountry());

		order.setShippingPostalCode(address.getPostalCode());

		order.setTotalAmount(finalAmount);

		orderDAO.save(order);

		Payment payment = new Payment();

		payment.setOrder(order);

		payment.setAmount(finalAmount);

		payment.setTransactionId(

				dto.getPaymentMethod()

						+ "-"

						+ System.currentTimeMillis());

		payment.setPaymentMethod(PaymentMethod.valueOf(dto.getPaymentMethod()));

		payment.setPaymentStatus(PaymentStatus.PENDING);

		System.out.println("REQUEST METHOD = " + dto.getPaymentMethod());

		PaymentMethod method = PaymentMethod.valueOf(dto.getPaymentMethod());

		System.out.println("ENUM METHOD = " + method);

		payment.setPaymentMethod(method);

		System.out.println("PAYMENT METHOD BEFORE SAVE = " + payment.getPaymentMethod());

		paymentDAO.save(payment);

		OrderItem item = new OrderItem();

		item.setOrder(order);

		item.setProductVariant(variant);

		item.setQuantity(dto.getQuantity());

		item.setPriceAtPurchase(variant.getPrice());

		item.setSubtotal(finalAmount);

		orderItemDAO.save(item);

		variant.setStock(variant.getStock() - dto.getQuantity());

		productVariantDAO.update(variant);

		PaymentMethod paymentMethod = PaymentMethod.valueOf(dto.getPaymentMethod());

		if (paymentMethod == PaymentMethod.COD)
			EmailService.sendOrderConfirmationEmail(order);

		return new ApiResponse(true, "Order placed successfully");
	}

	public OrderDetailsDTO getAdminOrderDetails(Long orderId) {

		Order order = orderDAO.findById(orderId);

		if (order == null)
			return null;

		List<OrderItem> items = orderItemDAO.findByOrder(order);

		List<OrderItemResponseDTO> itemDTOs = new ArrayList<>();

		for (OrderItem item : items) {

			OrderItemResponseDTO itemDTO = new OrderItemResponseDTO();

			ProductVariant variant = item.getProductVariant();

			itemDTO.setId(item.getId());

			itemDTO.setProductVariantId(variant.getId());

			itemDTO.setSku(variant.getSku());

			itemDTO.setProductId(variant.getProduct().getId());

			itemDTO.setProductName(variant.getProduct().getName());

			itemDTO.setQuantity(item.getQuantity());

			itemDTO.setPriceAtPurchase(item.getPriceAtPurchase());

			itemDTO.setSubtotal(item.getSubtotal());

			itemDTOs.add(itemDTO);
		}

		OrderDetailsDTO dto = new OrderDetailsDTO();

		dto.setOrder(order);
		dto.setItems(itemDTOs);

		return dto;
	}

	public RazorpayOrderResponseDTO createRazorpayOrder(Long orderId) {

		try {

			Order order = orderDAO.findById(orderId);

			if (order == null)
				return null;

			Payment payment = paymentDAO.findByOrder(order);

			com.razorpay.Order razorOrder = RazorpayUtil.createOrder(

					order.getTotalAmount().intValue());

			payment.setRazorpayOrderId(

					razorOrder.get("id").toString());

			paymentDAO.update(payment);

			RazorpayOrderResponseDTO dto = new RazorpayOrderResponseDTO();

			dto.setOrderId(order.getId());

			dto.setRazorpayOrderId(

					razorOrder.get("id").toString());

			dto.setAmount(

					order.getTotalAmount().doubleValue());

			return dto;

		} catch (Exception e) {

			e.printStackTrace();

			return null;
		}
	}

	public ApiResponse markRazorpaySuccess(RazorpaySuccessDTO dto) {

		Payment payment = paymentDAO.findByRazorpayOrderId(dto.getRazorpayOrderId());

		if (payment == null)
			return new ApiResponse(false, "Payment Not Found");

		payment.setRazorpayPaymentId(dto.getRazorpayPaymentId());

		payment.setRazorpaySignature(dto.getRazorpaySignature());

		payment.setPaymentStatus(PaymentStatus.SUCCESS);

		boolean paymentUpdated = paymentDAO.update(payment);

		Order order = payment.getOrder();

		order.setOrderStatus(OrderStatus.CONFIRMED);

		order.setPaymentStatus(PaymentStatus.SUCCESS);

		boolean orderUpdated = orderDAO.update(order);

		if (paymentUpdated && orderUpdated) {

			EmailService.sendOrderConfirmationEmail(order);
			EmailService.sendPaymentSuccessEmail(order);

			return new ApiResponse(true, "Payment successful and order confirmed.");
		}

		return new ApiResponse(false, "Unable to complete payment confirmation.");
	}

	public ApiResponse markPaymentFailed(Long orderId) {

		Order order = orderDAO.findById(orderId);

		if (order == null)
			return new ApiResponse(false, "Order Not Found");

		Payment payment = paymentDAO.findByOrder(order);

		if (payment == null)
			return new ApiResponse(false, "Payment not found.");

		payment.setPaymentStatus(PaymentStatus.FAILED);

		boolean updated = paymentDAO.update(payment);

		if (updated) {

			EmailService.sendOrderConfirmationEmail(order);

			EmailService.sendPaymentPendingEmail(order);

			return new ApiResponse(true, "Payment failed. You can retry the payment or cancel the order.");
		}

		return new ApiResponse(false, "Unable to update payment status.");
	}
}