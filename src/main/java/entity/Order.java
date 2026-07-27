package entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import entity.enums.OrderStatus;
import entity.enums.PaymentMethod;
import entity.enums.PaymentStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String shippingFullName;

    @Column(nullable = false)
    private String shippingMobileNumber;

    @Column(nullable = false)
    private String shippingAddressLine1;

    private String shippingAddressLine2;

    private String shippingLandmark;

    @Column(nullable = false)
    private String shippingCity;

    @Column(nullable = false)
    private String shippingState;

    @Column(nullable = false)
    private String shippingCountry;

    @Column(nullable = false)
    private String shippingPostalCode;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        orderStatus = OrderStatus.PENDING;
        paymentStatus = PaymentStatus.PENDING;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public Order() {
		
	}

	public Order(String orderNumber, User user, String shippingFullName, String shippingMobileNumber,
			String shippingAddressLine1, String shippingAddressLine2, String shippingLandmark, String shippingCity,
			String shippingState, String shippingCountry, String shippingPostalCode, BigDecimal totalAmount,
			OrderStatus orderStatus, PaymentStatus paymentStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
		
		this.orderNumber = orderNumber;
		this.user = user;
		this.shippingFullName = shippingFullName;
		this.shippingMobileNumber = shippingMobileNumber;
		this.shippingAddressLine1 = shippingAddressLine1;
		this.shippingAddressLine2 = shippingAddressLine2;
		this.shippingLandmark = shippingLandmark;
		this.shippingCity = shippingCity;
		this.shippingState = shippingState;
		this.shippingCountry = shippingCountry;
		this.shippingPostalCode = shippingPostalCode;
		this.totalAmount = totalAmount;
		this.orderStatus = orderStatus;
		this.paymentStatus = paymentStatus;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getShippingFullName() {
		return shippingFullName;
	}

	public void setShippingFullName(String shippingFullName) {
		this.shippingFullName = shippingFullName;
	}

	public String getShippingMobileNumber() {
		return shippingMobileNumber;
	}

	public void setShippingMobileNumber(String shippingMobileNumber) {
		this.shippingMobileNumber = shippingMobileNumber;
	}

	public String getShippingAddressLine1() {
		return shippingAddressLine1;
	}

	public void setShippingAddressLine1(String shippingAddressLine1) {
		this.shippingAddressLine1 = shippingAddressLine1;
	}

	public String getShippingAddressLine2() {
		return shippingAddressLine2;
	}

	public void setShippingAddressLine2(String shippingAddressLine2) {
		this.shippingAddressLine2 = shippingAddressLine2;
	}

	public String getShippingLandmark() {
		return shippingLandmark;
	}

	public void setShippingLandmark(String shippingLandmark) {
		this.shippingLandmark = shippingLandmark;
	}

	public String getShippingCity() {
		return shippingCity;
	}

	public void setShippingCity(String shippingCity) {
		this.shippingCity = shippingCity;
	}

	public String getShippingState() {
		return shippingState;
	}

	public void setShippingState(String shippingState) {
		this.shippingState = shippingState;
	}

	public String getShippingCountry() {
		return shippingCountry;
	}

	public void setShippingCountry(String shippingCountry) {
		this.shippingCountry = shippingCountry;
	}

	public String getShippingPostalCode() {
		return shippingPostalCode;
	}

	public void setShippingPostalCode(String shippingPostalCode) {
		this.shippingPostalCode = shippingPostalCode;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", orderNumber=" + orderNumber + ", user=" + user + ", shippingFullName="
				+ shippingFullName + ", shippingMobileNumber=" + shippingMobileNumber + ", shippingAddressLine1="
				+ shippingAddressLine1 + ", shippingAddressLine2=" + shippingAddressLine2 + ", shippingLandmark="
				+ shippingLandmark + ", shippingCity=" + shippingCity + ", shippingState=" + shippingState
				+ ", shippingCountry=" + shippingCountry + ", shippingPostalCode=" + shippingPostalCode
				+ ", totalAmount=" + totalAmount + ", orderStatus=" + orderStatus + ", paymentStatus=" + paymentStatus
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
    
    
}