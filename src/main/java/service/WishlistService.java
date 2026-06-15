package service;

import java.util.List;

import dao.ProductVariantDAO;
import dao.UserDAO;
import dao.WishlistItemDAO;
import dto.ApiResponse;
import dto.WishlistRequestDTO;
import entity.ProductVariant;
import entity.User;
import entity.Wishlist;

public class WishlistService {

	private WishlistItemDAO wishlistItemDAO;
	private UserDAO userDAO;
	private ProductVariantDAO productVariantDAO;

	public WishlistService() {

		wishlistItemDAO = new WishlistItemDAO();
		userDAO = new UserDAO();
		productVariantDAO = new ProductVariantDAO();
	}

	public ApiResponse addToWishlist(WishlistRequestDTO dto, String email) {

		User user = userDAO.findByEmail(email);

		if (user == null)
			return new ApiResponse(false, "User not found.");

		ProductVariant variant = productVariantDAO.findById(dto.getProductVariantId());

		if (variant == null)
			return new ApiResponse(false, "Variant not found.");

		Wishlist existingWishlistItem = wishlistItemDAO.findByUserAndVariant(user, variant);

		if (existingWishlistItem != null)
			return new ApiResponse(false, "Already in wishlist.");

		Wishlist wishlistItem = new Wishlist();

		wishlistItem.setUser(user);
		wishlistItem.setProductVariant(variant);

		boolean status = wishlistItemDAO.save(wishlistItem);

		return status ? new ApiResponse(true, "Added to wishlist.")
				: new ApiResponse(false, "Failed to add wishlist item.");
	}

	public List<Wishlist> getWishlist(String email) {

		User user = userDAO.findByEmail(email);

		if (user == null)
			return List.of();

		return wishlistItemDAO.findByUser(user);
	}

	public ApiResponse removeFromWishlist(Long wishlistId, String email) {

		User user = userDAO.findByEmail(email);

		if (user == null)
			return new ApiResponse(false, "User not found.");

		Wishlist wishlistItem = wishlistItemDAO.findById(wishlistId);

		if (wishlistItem == null)
			return new ApiResponse(false, "Wishlist item not found.");

		if (!wishlistItem.getUser().getId().equals(user.getId()))
			return new ApiResponse(false, "Unauthorized access.");

		boolean status = wishlistItemDAO.delete(wishlistItem);

		return status ? new ApiResponse(true, "Removed from wishlist.")
				: new ApiResponse(false, "Failed to remove wishlist item.");
	}
}