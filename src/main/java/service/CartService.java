package service;

import java.util.ArrayList;
import java.util.List;

import dao.CartDAO;
import dao.CartItemDAO;
import dao.ProductVariantDAO;
import dao.UserDAO;
import dto.AddToCartRequestDTO;
import dto.ApiResponse;
import dto.CartItemResponseDTO;
import entity.Cart;
import entity.CartItem;
import entity.Product;
import entity.ProductVariant;
import entity.User;

public class CartService {

	CartDAO cartDAO;
	UserDAO userDAO;
	ProductVariantDAO productVariantDAO;
	CartItemDAO cartItemDAO;

	public CartService() {
		cartDAO = new CartDAO();
		userDAO = new UserDAO();
		productVariantDAO = new ProductVariantDAO();
		cartItemDAO = new CartItemDAO();
	}

	public ApiResponse addToCart(AddToCartRequestDTO dto, String email) {

		if (dto.getQuantity() <= 0)
			return new ApiResponse(false, "Quantity must be greater than zero.");

		User user = userDAO.findByEmail(email);

		if (user == null)
			return new ApiResponse(false, "User not found.");

		Cart cart = cartDAO.findByUser(user);

		if (cart == null) {
			cart = new Cart();
			cart.setUser(user);
			cartDAO.save(cart);
		}

		ProductVariant productVariant = productVariantDAO.findById(dto.getProductVariantId());

		if (productVariant == null)
			return new ApiResponse(false, "product variant not found.");

		if (!productVariant.getProduct().isActive()) {
			return new ApiResponse(false, "Product is unavailable.");
		}

		if (!productVariant.isActive())
			return new ApiResponse(false, "product variant not active.");

		if (productVariant.getStock() <= 0)
			return new ApiResponse(false, "currently product is out of stock.");

		if (productVariant.getStock() < dto.getQuantity())
			return new ApiResponse(false, "current stock is less then requested quantity");

		CartItem cartItem = cartItemDAO.findByCartAndProductVariant(cart, productVariant);

		if (cartItem == null) {
			CartItem newCartItem = new CartItem();
			newCartItem.setCart(cart);
			newCartItem.setProductVariant(productVariant);
			newCartItem.setQuantity(dto.getQuantity());

			cartItemDAO.save(newCartItem);
		} else {
			int newQuantity = cartItem.getQuantity() + dto.getQuantity();
			if (newQuantity <= productVariant.getStock()) {
				cartItem.setQuantity(newQuantity);
				cartItemDAO.update(cartItem);
			} else
				return new ApiResponse(false, "current stock is less then requested quantity");
		}

		return new ApiResponse(true, "product added to cart successfully.");

	}

	public List<CartItemResponseDTO> getCart(String email) {

		User user = userDAO.findByEmail(email);

		if (user == null)
			return List.of();

		Cart cart = cartDAO.findByUser(user);

		if (cart == null)
			return List.of();

		List<CartItem> cartItems = cartItemDAO.findByCart(cart);

		List<CartItemResponseDTO> responseList = new ArrayList<>();

		for (CartItem item : cartItems) {

			CartItemResponseDTO dto = new CartItemResponseDTO();

			dto.setId(item.getId());

			dto.setQuantity(item.getQuantity());

			ProductVariant variant = item.getProductVariant();

			dto.setProductVariantId(variant.getId());

			dto.setSku(variant.getSku());

			dto.setPrice(variant.getPrice());

			Product product = variant.getProduct();

			dto.setProductId(product.getId());

			dto.setProductName(product.getName());

			responseList.add(dto);
		}

		return responseList;
	}

	public ApiResponse updateQuantity(Long cartItemId, Integer quantity, String email) {

		User user = userDAO.findByEmail(email);

		if (user == null) {
			return new ApiResponse(false, "User not found.");
		}

		if (quantity <= 0)
			return new ApiResponse(false, "Quantity must be greater than zero.");

		System.out.println("Cart Item ID = " + cartItemId);
		System.out.println("Quantity = " + quantity);

		CartItem cartItem = cartItemDAO.findById(cartItemId);

		if (cartItem == null)
			return new ApiResponse(false, "Cart item not found.");

		if (!cartItem.getCart().getUser().getId().equals(user.getId())) {
			return new ApiResponse(false, "Unauthorized access.");
		}

		ProductVariant variant = cartItem.getProductVariant();

		if (quantity > variant.getStock())
			return new ApiResponse(false, "Requested quantity exceeds stock.");

		cartItem.setQuantity(quantity);

		boolean updateStatus = cartItemDAO.update(cartItem);

		return updateStatus ? new ApiResponse(true, "Quantity updated successfully.")
				: new ApiResponse(false, "Unable to update quantity.");
	}

	public ApiResponse removeItem(Long cartItemId, String email) {

		User user = userDAO.findByEmail(email);

		if (user == null) {
			return new ApiResponse(false, "User not found.");
		}

		CartItem cartItem = cartItemDAO.findById(cartItemId);

		if (cartItem == null)
			return new ApiResponse(false, "Cart item not found.");

		if (!cartItem.getCart().getUser().getId().equals(user.getId())) {
			return new ApiResponse(false, "Unauthorized access.");
		}

		boolean deleteStatus = cartItemDAO.delete(cartItem);

		return deleteStatus ? new ApiResponse(true, "Item removed successfully.")
				: new ApiResponse(false, "Unable to remove item.");
	}

	public ApiResponse clearCart(String email) {

		User user = userDAO.findByEmail(email);

		if (user == null)
			return new ApiResponse(false, "User not found.");

		Cart cart = cartDAO.findByUser(user);

		if (cart == null)
			return new ApiResponse(false, "Cart not found.");

		if (!cart.getUser().getId().equals(user.getId())) {
			return new ApiResponse(false, "Unauthorized access.");
		}

		boolean status = cartItemDAO.deleteAllByCart(cart);

		return status ? new ApiResponse(true, "Cart cleared successfully.")
				: new ApiResponse(false, "Unable to clear cart.");
	}

}
