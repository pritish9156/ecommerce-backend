package dto;

public class PlaceOrderRequestDTO {

    private Long addressId;

    public PlaceOrderRequestDTO() {
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }
}