package dto;

public class AuthResponse {

    private boolean success;
    private String message;
    private String token;
    private String role;
    private Long userId;

    public AuthResponse() {

    }

    public AuthResponse(
            boolean success,
            String message,
            String token,
            String role,
            Long userId) {

        this.success = success;
        this.message = message;
        this.token = token;
        this.role = role;
        this.userId = userId;
    }

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public Long getUserId() {
	    return userId;
	}

	public void setUserId(Long userId) {
	    this.userId = userId;
	}

}