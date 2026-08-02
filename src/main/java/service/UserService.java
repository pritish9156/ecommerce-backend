package service;

import java.util.List;

import dao.UserDAO;
import dto.UpdateProfileDTO;
import dto.UpdateProfileImageDTO;
import dto.UpdateUserStatusDTO;
import dto.response.ApiResponse;
import entity.User;

public class UserService {

	UserDAO userDAO;

	public UserService() {
		userDAO = new UserDAO();
	}

	public ApiResponse getProfile(String email) {

		User user = userDAO.findByEmail(email);
		
		if(user == null)
			return new ApiResponse(false, "user not found");
		else
			return new ApiResponse(true, "user found.", user);
	}

	public List<User> getAllUsers() {

		return userDAO.findAll();
	}

	public ApiResponse updateUserStatus(UpdateUserStatusDTO dto) {

		User user = userDAO.findById(dto.getUserId());

		if (user == null)
			return new ApiResponse(false, "User not found");

		user.setActive(dto.isActive());

		boolean updated = userDAO.update(user);

		return updated

				?

				new ApiResponse(true, "User Updated")

				:

				new ApiResponse(false, "Update Failed");
	}

	public ApiResponse updateProfile(String email, UpdateProfileDTO dto) {

		User user = userDAO.findByEmail(email);

		if (user == null)
			return new ApiResponse(false, "user not found.");

		String firstName = dto.getFirstName().trim();
		String lastName = dto.getLastName().trim();
		String mobileNumber = dto.getMobileNumber().trim();

		System.out.println(firstName);
		System.out.println(lastName);
		System.out.println(mobileNumber);

		if (firstName.isEmpty() || lastName.isEmpty() || mobileNumber.isEmpty())
			return new ApiResponse(false, "required data is not provided correctly.");

		User userMobileCheck = userDAO.findByMobileNumber(mobileNumber);

		if (userMobileCheck != null) {
			if (!userMobileCheck.getId().equals(user.getId()))
				return new ApiResponse(false, "mobile number is already register with some other account.");
		}

		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setMobileNumber(mobileNumber);

		boolean updateStatus = userDAO.update(user);

		if (updateStatus)
			return new ApiResponse(true, "Profile updated successfully.");
		else
			return new ApiResponse(false, "Profile updation failed some error occurred.");
	}

	public ApiResponse updateProfileImage(String email, UpdateProfileImageDTO dto) {

		User user = userDAO.findByEmail(email);

		if (user == null)
			return new ApiResponse(false, "user not found.");

		if (dto.getProfileImage() == null && dto.getProfileImage().trim().isEmpty())
			return new ApiResponse(false, "Profile image is required.");

		String profileImage = dto.getProfileImage().trim();

		user.setProfileImage(profileImage);

		boolean updateStatus = userDAO.update(user);

		if (updateStatus)
			return new ApiResponse(true, "profile picture updated.");
		else
			return new ApiResponse(false, "failed to update profile picture.");

	}
}
