package entity;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(
            name = "product_variant_id",
            nullable = false
    )
    private ProductVariant productVariant;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal priceAtPurchase;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

	public OrderItem(Order order, ProductVariant productVariant, Integer quantity, BigDecimal priceAtPurchase,
			BigDecimal subtotal) {
		super();
		this.order = order;
		this.productVariant = productVariant;
		this.quantity = quantity;
		this.priceAtPurchase = priceAtPurchase;
		this.subtotal = subtotal;
	}
    
    
    public OrderItem() {
		// TODO Auto-generated constructor stub
	}


	public Order getOrder() {
		return order;
	}


	public void setOrder(Order order) {
		this.order = order;
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


	public BigDecimal getPriceAtPurchase() {
		return priceAtPurchase;
	}


	public void setPriceAtPurchase(BigDecimal priceAtPurchase) {
		this.priceAtPurchase = priceAtPurchase;
	}


	public BigDecimal getSubtotal() {
		return subtotal;
	}


	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}


	public Long getId() {
		return id;
	}


	@Override
	public String toString() {
		return "OrderItem [id=" + id + ", order=" + order + ", productVariant=" + productVariant + ", quantity="
				+ quantity + ", priceAtPurchase=" + priceAtPurchase + ", subtotal=" + subtotal + "]";
	}
    
}