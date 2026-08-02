package dto;

public class UpdateProfileImageDTO {

	private String profileImage;
	
	public UpdateProfileImageDTO() {
		
	}

	public void setProfileImage(String imageUrl) {
		this.profileImage = imageUrl;
	}

	public String getProfileImage() {	
		return profileImage;
	}
	
}
