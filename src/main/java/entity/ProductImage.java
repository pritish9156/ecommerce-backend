package entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
	    name = "product_images",
	    uniqueConstraints = {
	        @UniqueConstraint(
	            columnNames = {
	                "product_id",
	                "display_order"
	            }
	        )
	    }
	)
public class ProductImage {
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;

	 @ManyToOne
	 @JoinColumn(name = "product_id", nullable = false)
	 private Product product;

	 @Column(nullable = false, length = 1000)
	 private String imageUrl;

	 @Column(name = "display_order", nullable = false)
	 private Integer displayOrder;

	 private String altText;

	 @Column(nullable = false, updatable = false)
	 private LocalDateTime createdAt;

	 @PrePersist
	 public void preCreate() {
	     createdAt = LocalDateTime.now();
	 }

	 public ProductImage(Product product, String imageUrl, Integer displayOrder, String altText,
			LocalDateTime createdAt) {

		this.product = product;
		this.imageUrl = imageUrl;
		this.displayOrder = displayOrder;
		this.altText = altText;
		this.createdAt = createdAt;
	 }
	 
	 public ProductImage() {
		// TODO Auto-generated constructor stub
	}

	 public Product getProduct() {
		 return product;
	 }

	 public void setProduct(Product product) {
		 this.product = product;
	 }

	 public String getImageUrl() {
		 return imageUrl;
	 }

	 public void setImageUrl(String imageUrl) {
		 this.imageUrl = imageUrl;
	 }

	 public Integer getDisplayOrder() {
		 return displayOrder;
	 }

	 public void setDisplayOrder(Integer displayOrder) {
		 this.displayOrder = displayOrder;
	 }

	 public String getAltText() {
		 return altText;
	 }

	 public void setAltText(String altText) {
		 this.altText = altText;
	 }

	 public Long getId() {
		 return id;
	 }

	 public LocalDateTime getCreatedAt() {
		 return createdAt;
	 }

	 @Override
	 public String toString() {
		return "ProductImage [id=" + id + ", product=" + product + ", imageUrl=" + imageUrl + ", displayOrder="
				+ displayOrder + ", altText=" + altText + ", createdAt=" + createdAt + "]";
	 } 
	 
}