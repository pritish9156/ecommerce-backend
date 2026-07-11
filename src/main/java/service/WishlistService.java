package service;

import java.util.ArrayList;
import java.util.List;

import dao.ProductImageDAO;
import dao.ProductVariantDAO;
import dao.UserDAO;
import dao.WishlistItemDAO;
import dto.ApiResponse;
import dto.WishlistRequestDTO;
import dto.WishlistResponseDTO;
import entity.ProductImage;
import entity.ProductVariant;
import entity.User;
import entity.Wishlist;

public class WishlistService {

	private WishlistItemDAO wishlistItemDAO;
	private UserDAO userDAO;
	private ProductVariantDAO productVariantDAO;
	private ProductImageDAO productImageDAO;

	public WishlistService() {

		wishlistItemDAO = new WishlistItemDAO();
		userDAO = new UserDAO();
		productVariantDAO = new ProductVariantDAO();
		productImageDAO = new ProductImageDAO();
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

	public List<WishlistResponseDTO> getWishlist(String email) {

		User user = userDAO.findByEmail(email);

		if (user == null)
			return List.of();

		List<Wishlist> wishlist = wishlistItemDAO.findByUser(user);

		List<WishlistResponseDTO> response = new ArrayList<>();

		for (Wishlist item : wishlist) {

			WishlistResponseDTO dto = new WishlistResponseDTO();

			dto.setId(item.getId());

			dto.setVariantId(item.getProductVariant().getId());

			dto.setProductId(item.getProductVariant().getProduct().getId());

			dto.setProductName(item.getProductVariant().getProduct().getName());

			dto.setSku(item.getProductVariant().getSku());

			dto.setPrice(item.getProductVariant().getPrice());

			ProductImage image = productImageDAO.findFirstByProduct(item.getProductVariant().getProduct());

			if (image != null) {

				dto.setImageUrl(image.getImageUrl());
			}

			response.add(dto);
		}

		return response;
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