package dto.request;

public class UpdateCartRequestDTO {

	private Long cartItemId;
    private Integer quantity;
    
    public UpdateCartRequestDTO() {
		
	}
    
	public UpdateCartRequestDTO(Long cartItemId, Integer quantity) {
		super();
		this.cartItemId = cartItemId;
		this.quantity = quantity;
	}

	public Long getCartItemId() {
		return cartItemId;
	}

	public void setCartItemId(Long cartItemId) {
		this.cartItemId = cartItemId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "UpdateCartRequestDTO [cartItemId=" + cartItemId + ", quantity=" + quantity + "]";
	}
    
    
}
