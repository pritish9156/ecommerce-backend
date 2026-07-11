package dto;

import java.math.BigDecimal;

import entity.enums.OrderStatus;

public class RecentOrderDTO {

    private String orderNumber;

    private String customerName;

    private BigDecimal totalAmount;

    private OrderStatus orderStatus;
    
    public RecentOrderDTO() {
		// TODO Auto-generated constructor stub
	}

	public RecentOrderDTO(String orderNumber, String customerName, BigDecimal totalAmount, OrderStatus orderStatus) {
		super();
		this.orderNumber = orderNumber;
		this.customerName = customerName;
		this.totalAmount = totalAmount;
		this.orderStatus = orderStatus;
	}
	
	

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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

	@Override
	public String toString() {
		return "RecentOrderDTO [orderNumber=" + orderNumber + ", customerName=" + customerName + ", totalAmount="
				+ totalAmount + ", orderStatus=" + orderStatus + "]";
	}
    
    

}