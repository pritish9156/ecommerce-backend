package service;

import java.util.List;

import dao.UserDAO;
import dto.ApiResponse;
import dto.UpdateUserStatusDTO;
import entity.User;

public class UserService {

	UserDAO userDAO;

	public UserService() {
		userDAO = new UserDAO();
	}

	public User getProfile(String email) {

		return userDAO.findByEmail(email);
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
}
