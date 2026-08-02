package dto.request;

public class ReviewRequestDTO {

    private Long id;

    private Long productVariantId;

    private Integer rating;

    private String reviewTitle;

    private String reviewText;
    
    public ReviewRequestDTO() {
		
	}

	public ReviewRequestDTO(Long id, Long productVariantId, Integer rating, String reviewTitle, String reviewText) {
		super();
		this.id = id;
		this.productVariantId = productVariantId;
		this.rating = rating;
		this.reviewTitle = reviewTitle;
		this.reviewText = reviewText;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductVariantId() {
		return productVariantId;
	}

	public void setProductVariantId(Long productVariantId) {
		this.productVariantId = productVariantId;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getReviewTitle() {
		return reviewTitle;
	}

	public void setReviewTitle(String reviewTitle) {
		this.reviewTitle = reviewTitle;
	}

	public String getReviewText() {
		return reviewText;
	}

	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}

    
}