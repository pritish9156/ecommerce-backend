package dto.request;

public class AddToCartRequestDTO {

	private Long productVariantId;
	private Integer quantity;
	
	public AddToCartRequestDTO() {
		
	}
	
	public AddToCartRequestDTO(Long productVariantId, Integer quantity) {
		this.productVariantId = productVariantId;
		this.quantity = quantity;
	}

	public Long getProductVariantId() {
		return productVariantId;
	}
	public void setProductVariantId(Long productVariantId) {
		this.productVariantId = productVariantId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "AddToCartRequestDTO [productVariantId=" + productVariantId + ", quantity=" + quantity + "]";
	}
	
	
}
