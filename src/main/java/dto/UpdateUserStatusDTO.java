package dto;

public class UpdateUserStatusDTO {

	private Long userId;

	private boolean active;

	public UpdateUserStatusDTO() {

	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}