package dto.response;

public class ProfileDataResponseDTO {
	
	private Long orderCount;
	private Long wishlistCount;
	private Long reviewsCount;
	private Long addressCount;
	
	public ProfileDataResponseDTO() {
		
	}

	public Long getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Long orderCount) {
		this.orderCount = orderCount;
	}

	public Long getWishlistCount() {
		return wishlistCount;
	}

	public void setWishlistCount(Long wishlistCount) {
		this.wishlistCount = wishlistCount;
	}

	public Long getReviewsCount() {
		return reviewsCount;
	}

	public void setReviewsCount(Long reviewsCount) {
		this.reviewsCount = reviewsCount;
	}

	public Long getAddressCount() {
		return addressCount;
	}

	public void setAddressCount(Long addressCount) {
		this.addressCount = addressCount;
	}
	
}
