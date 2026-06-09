package entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(
    name = "reviews",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {
                "user_id",
                "product_variant_id"
            }
        )
    }
)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @ManyToOne
    @JoinColumn(
            name = "product_variant_id",
            nullable = false
    )
    private ProductVariant productVariant;

    @Column(nullable = false)
    private Integer rating;

    @Column(length = 200)
    private String reviewTitle;

    @Column(length = 2000)
    private String reviewText;

    @Column(nullable = false)
    private boolean isVerifiedPurchase;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        isVerifiedPurchase = false;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

	public Review(User user, ProductVariant productVariant, Integer rating, String reviewTitle, String reviewText,
			boolean isVerifiedPurchase, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.user = user;
		this.productVariant = productVariant;
		this.rating = rating;
		this.reviewTitle = reviewTitle;
		this.reviewText = reviewText;
		this.isVerifiedPurchase = isVerifiedPurchase;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
    
    public Review() {
		// TODO Auto-generated constructor stub
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ProductVariant getProductVariant() {
		return productVariant;
	}

	public void setProductVariant(ProductVariant productVariant) {
		this.productVariant = productVariant;
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

	public boolean isVerifiedPurchase() {
		return isVerifiedPurchase;
	}

	public void setVerifiedPurchase(boolean isVerifiedPurchase) {
		this.isVerifiedPurchase = isVerifiedPurchase;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	@Override
	public String toString() {
		return "Review [id=" + id + ", user=" + user + ", productVariant=" + productVariant + ", rating=" + rating
				+ ", reviewTitle=" + reviewTitle + ", reviewText=" + reviewText + ", isVerifiedPurchase="
				+ isVerifiedPurchase + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
    
}