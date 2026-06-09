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
	    name = "cart_items",
	    uniqueConstraints = {
	        @UniqueConstraint(
	            columnNames = {
	                "cart_id",
	                "product_variant_id"
	            }
	        )
	    }
	)
public class CartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "cart_id", nullable = false)
	private Cart cart;

	@ManyToOne
	@JoinColumn(
	    name = "product_variant_id",
	    nullable = false
	)
	private ProductVariant productVariant;

	@Column(nullable = false)
	private Integer quantity;

	private LocalDateTime addedAt;
	
	@PrePersist
	public void preCreate() {
		addedAt = LocalDateTime.now();
	}

	public CartItem(Cart cart, ProductVariant productVariant, Integer quantity, LocalDateTime addedAt) {
		super();
		this.cart = cart;
		this.productVariant = productVariant;
		this.quantity = quantity;
		this.addedAt = addedAt;
	}
	
	public CartItem() {
		
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public ProductVariant getProductVariant() {
		return productVariant;
	}

	public void setProductVariant(ProductVariant productVariant) {
		this.productVariant = productVariant;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getAddedAt() {
		return addedAt;
	}

	@Override
	public String toString() {
		return "CartItem [id=" + id + ", cart=" + cart + ", productVariant=" + productVariant + ", quantity=" + quantity
				+ ", addedAt=" + addedAt + "]";
	}
	
}
