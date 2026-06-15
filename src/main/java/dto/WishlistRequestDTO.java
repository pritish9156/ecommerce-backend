package dto;

public class WishlistRequestDTO {
	
	private Long productVariantId;
	
	public WishlistRequestDTO() {
		
	}

	public WishlistRequestDTO(Long productVariantId) {
		this.productVariantId = productVariantId;
	}

	public Long getProductVariantId() {
		return productVariantId;
	}

	public void setProductVariantId(Long productVariantId) {
		this.productVariantId = productVariantId;
	}
	

}
