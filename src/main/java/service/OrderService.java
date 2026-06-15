package service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import dao.AddressDAO;
import dao.CartDAO;
import dao.CartItemDAO;
import dao.OrderDAO;
import dao.OrderItemDAO;
import dao.ProductVariantDAO;
import dao.UserDAO;
import dto.ApiResponse;
import dto.PlaceOrderRequestDTO;
import entity.Address;
import entity.Cart;
import entity.CartItem;
import entity.Order;
import entity.OrderItem;
import entity.ProductVariant;
import entity.User;

public class OrderService {

	private UserDAO userDAO;
	private AddressDAO addressDAO;
	private CartDAO cartDAO;
	private CartItemDAO cartItemDAO;
	private OrderDAO orderDAO;
	private OrderItemDAO orderItemDAO;
	private ProductVariantDAO productVariantDAO;

	public OrderService() {

		userDAO = new UserDAO();
		addressDAO = new AddressDAO();
		cartDAO = new CartDAO();
		cartItemDAO = new CartItemDAO();
		orderDAO = new OrderDAO();
		orderItemDAO = new OrderItemDAO();
		productVariantDAO = new ProductVariantDAO();
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

		order.setTotalAmount(totalAmount);

		boolean orderSaved = orderDAO.save(order);

		if (!orderSaved)
			return new ApiResponse(false, "Unable to place order.");

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

		return new ApiResponse(true, "Order placed successfully. Order Number: " + order.getOrderNumber());
	}

	public List<Order> getOrders(String email) {

		User user = userDAO.findByEmail(email);

		if (user == null)
			return List.of();

		return orderDAO.findByUser(user);
	}
}