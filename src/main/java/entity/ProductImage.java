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
}