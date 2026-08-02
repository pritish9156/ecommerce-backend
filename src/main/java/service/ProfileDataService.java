package service;

import dao.ProfileDataDAO;
import dto.response.ApiResponse;
import dto.response.ProfileDataResponseDTO;
import entity.User;

public class ProfileDataService {
	
	ProfileDataDAO profileDataDAO;
	UserService userService;
	
	public ProfileDataService() {
		profileDataDAO = new ProfileDataDAO();
		userService = new UserService();
	}

	public ApiResponse getProfileData(String email) {
		
		ApiResponse response = userService.getProfile(email);
		
		if(!response.isSuccess()) {
			return new ApiResponse(false, "user not found");
		}
		
		User user = (User) response.getData();
		
		Long orderCount = profileDataDAO.getOrderCount(user);
		
		Long wishlistCount = profileDataDAO.getWishlistCount(user);
		
		Long ReviewsCount = profileDataDAO.getReviewsCount(user);
		
		Long AddressCount = profileDataDAO.getAddressCount(user);
		
		ProfileDataResponseDTO dto = new ProfileDataResponseDTO();
		
		dto.setOrderCount(orderCount);
		
		dto.setWishlistCount(wishlistCount);
		
		dto.setReviewsCount(ReviewsCount);
		
		dto.setAddressCount(AddressCount);
		
		return new ApiResponse(true, "profile data fetched successfully", dto);
		
	}

}
