package service;

import java.util.List;

import dao.AddressDAO;
import dao.UserDAO;
import dto.request.AddressRequestDTO;
import dto.response.ApiResponse;
import entity.Address;
import entity.User;

public class AddressService {

	private AddressDAO addressDAO;
	private UserDAO userDAO;

	public AddressService() {
		addressDAO = new AddressDAO();
		userDAO = new UserDAO();
	}

	public ApiResponse addAddress(AddressRequestDTO dto, String email) {

		User user = userDAO.findByEmail(email);

		if (user == null) {
			return new ApiResponse(false, "User not found.");
		}

		Address address = new Address();

		address.setUser(user);
		address.setFullName(dto.getFullName());
		address.setMobileNumber(dto.getMobileNumber());
		address.setAddressLine1(dto.getAddressLine1());
		address.setAddressLine2(dto.getAddressLine2());
		address.setLandmark(dto.getLandmark());
		address.setCity(dto.getCity());
		address.setState(dto.getState());
		address.setCountry(dto.getCountry());
		address.setPostalCode(dto.getPostalCode());
		address.setDefault(dto.isDefault());
		
		if(addressDAO.existsAddress(address)) {

		    return new ApiResponse(
		        false,
		        "Address already exists.");
		}

		boolean saved = addressDAO.save(address);

		return saved ? new ApiResponse(true, "Address added successfully.")
				: new ApiResponse(false, "Failed to add address.");
	}

	public List<Address> getUserAddresses(String email) {

		User user = userDAO.findByEmail(email);

		if (user == null) {
			return List.of();
		}

		return addressDAO.findByUser(user);
	}

	public ApiResponse updateAddress(AddressRequestDTO dto, String email) {

		User user = userDAO.findByEmail(email);

		if (user == null) {
			return new ApiResponse(false, "User not found.");
		}
		
		System.out.println("DTO ID = " + dto.getId());

		Address existingAddress = addressDAO.findById(dto.getId());

		if (existingAddress == null) {
			return new ApiResponse(false, "Address not found.");
		}

		if (!existingAddress.getUser().getId().equals(user.getId())) {
			return new ApiResponse(false, "Unauthorized.");
		}

		existingAddress.setFullName(dto.getFullName());
		existingAddress.setMobileNumber(dto.getMobileNumber());
		existingAddress.setAddressLine1(dto.getAddressLine1());
		existingAddress.setAddressLine2(dto.getAddressLine2());
		existingAddress.setLandmark(dto.getLandmark());
		existingAddress.setCity(dto.getCity());
		existingAddress.setState(dto.getState());
		existingAddress.setCountry(dto.getCountry());
		existingAddress.setPostalCode(dto.getPostalCode());
		existingAddress.setDefault(dto.isDefault());

		boolean updated = addressDAO.update(existingAddress);

		return updated ? new ApiResponse(true, "Address updated successfully.")
				: new ApiResponse(false, "Failed to update address.");
	}

	public ApiResponse deleteAddress(Long addressId, String email) {

		User user = userDAO.findByEmail(email);

		if (user == null) {
			return new ApiResponse(false, "User not found.");
		}

		Address address = addressDAO.findById(addressId);

		if (address == null) {
			return new ApiResponse(false, "Address not found.");
		}

		if (!address.getUser().getId().equals(user.getId())) {
			return new ApiResponse(false, "Unauthorized.");
		}

		boolean deleted = addressDAO.delete(addressId);

		return deleted ? new ApiResponse(true, "Address deleted successfully.")
				: new ApiResponse(false, "Failed to delete address.");
	}
}