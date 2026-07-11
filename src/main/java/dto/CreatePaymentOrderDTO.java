package dto;

public class CreatePaymentOrderDTO {

    private Long orderId;

    public CreatePaymentOrderDTO() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}