package dto;
import entity.enums.PaymentStatus;

public class UpdatePaymentStatusDTO {

    private Long paymentId;

    private PaymentStatus paymentStatus;

    public UpdatePaymentStatusDTO() {
    }

	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

    
}