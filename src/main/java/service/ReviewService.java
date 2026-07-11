package service;

import java.util.List;

import dao.OrderItemDAO;
import dao.ProductDAO;
import dao.ProductVariantDAO;
import dao.ReviewDAO;
import dao.UserDAO;
import dto.ApiResponse;
import dto.ReviewRequestDTO;
import entity.ProductVariant;
import entity.Review;
import entity.User;

public class ReviewService {

	ReviewDAO reviewDAO;
	UserDAO userDAO;
	ProductVariantDAO productVariantDAO;
	OrderItemDAO orderItemDAO;
	ProductDAO productDAO;

	public ReviewService() {
		reviewDAO = new ReviewDAO();
		userDAO = new UserDAO();
		productVariantDAO = new ProductVariantDAO();
		orderItemDAO = new OrderItemDAO();
		productDAO = new ProductDAO();
	}

	private void recalculateProductRating(ProductVariant variant) {

		List<Review> reviews = reviewDAO.findByVariant(variant);

		double totalRating = 0;

		for (Review r : reviews) {
			totalRating += r.getRating();
		}

		if (reviews.isEmpty()) {

			variant.getProduct().setAverageRating(0);

			variant.getProduct().setReviewCount(0);

			productDAO.update(variant.getProduct());

			return;
		}

		double averageRating = totalRating / reviews.size();

		variant.getProduct().setAverageRating(averageRating);

		variant.getProduct().setReviewCount(reviews.size());

		productDAO.update(variant.getProduct());

	}

	public ApiResponse addReview(ReviewRequestDTO dto, String email) {

		User user = userDAO.findByEmail(email);

		ProductVariant variant = productVariantDAO.findById(dto.getProductVariantId());

		if (user == null)
			return new ApiResponse(false, "user not found");

		Review existingReview = reviewDAO.findByUserAndVariant(user, variant);

		if (existingReview != null)
			return new ApiResponse(false, "You already reviewed this product.");

		boolean isVerifiedPurchased = orderItemDAO.hasPurchasedVariant(user, variant);

		if (!isVerifiedPurchased)
			return new ApiResponse(false, "You have not purchases this product yet.");

		Review review = new Review();

		review.setUser(user);
		review.setProductVariant(variant);
		review.setRating(dto.getRating());
		review.setReviewTitle(dto.getReviewTitle());
		review.setReviewText(dto.getReviewText());
		review.setVerifiedPurchase(true);

		if (dto.getRating() < 1 || dto.getRating() > 5) {
			return new ApiResponse(false, "Rating must be between 1 and 5.");
		}

		boolean isSaved = reviewDAO.save(review);

		if (!isSaved)
			return new ApiResponse(false, "failed to add review.");

		recalculateProductRating(review.getProductVariant());

		return new ApiResponse(true, "Review added successfully.");
	}

	public ApiResponse updateReview(ReviewRequestDTO dto, String email) {

		User user = userDAO.findByEmail(email);

		if (user == null)
			return new ApiResponse(false, "User not found.");

		Review review = reviewDAO.findById(dto.getId());

		if (review == null)
			return new ApiResponse(false, "Review not found.");

		if (!review.getUser().getId().equals(user.getId()))
			return new ApiResponse(false, "Unauthorized access.");

		if (dto.getRating() < 1 || dto.getRating() > 5)
			return new ApiResponse(false, "Rating must be between 1 and 5.");

		review.setRating(dto.getRating());

		review.setReviewTitle(dto.getReviewTitle());

		review.setReviewText(dto.getReviewText());

		boolean updateStatus = reviewDAO.update(review);

		if (!updateStatus)
			return new ApiResponse(false, "Failed to update review.");

		recalculateProductRating(review.getProductVariant());

		return new ApiResponse(true, "Review updated successfully.");
	}

	public ApiResponse deleteReview(Long reviewId, String email) {

		User user = userDAO.findByEmail(email);

		if (user == null)
			return new ApiResponse(false, "User not found.");

		Review review = reviewDAO.findById(reviewId);

		if (review == null)
			return new ApiResponse(false, "Review not found.");

		if (!review.getUser().getId().equals(user.getId()))
			return new ApiResponse(false, "Unauthorized access.");

		ProductVariant variant = review.getProductVariant();

		boolean deleteStatus = reviewDAO.delete(review);

		if (!deleteStatus)
			return new ApiResponse(false, "Failed to delete review.");

		recalculateProductRating(variant);

		return new ApiResponse(true, "Review deleted successfully.");
	}
}
