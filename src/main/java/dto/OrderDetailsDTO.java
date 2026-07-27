package dto;

import java.util.List;

import entity.Order;
import entity.OrderItem;
import entity.enums.PaymentMethod;
import entity.enums.PaymentStatus;

public class OrderDetailsDTO {

	private Order order;

    private List<OrderItemResponseDTO> items;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private String transactionId;

    public OrderDetailsDTO() {
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderItemResponseDTO> getItems() {
        return items;
    }

    public void setItems(
            List<OrderItemResponseDTO> items) {
        this.items = items;
    }

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
    
}