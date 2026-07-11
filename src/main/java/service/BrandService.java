package service;

import java.util.List;

import dao.BrandDAO;
import dto.ApiResponse;
import dto.BrandRequestDTO;
import entity.Brand;

public class BrandService {

	BrandDAO brandDAO;

	public BrandService() {
		brandDAO = new BrandDAO();
	}

	private String generateSlug(String text) {

		return text.toLowerCase().trim().replaceAll("\\s+", "-").replaceAll("[^a-z0-9-]", "");
	}

	public ApiResponse addBrandDetails(BrandRequestDTO dto) {

		Brand brand = brandDAO.findBrandByName(dto.getName());

		if (brand != null)
			return new ApiResponse(false, "brand already exists");

		brand = brandDAO.findBrandBySlug(dto.getSlug());

		if (brand != null)
			return new ApiResponse(false, "brand already exists");

		brand = new Brand();

		brand.setName(dto.getName());
		brand.setDescription(dto.getDescription());
		brand.setSlug(generateSlug(dto.getName()));
		brand.setCountry(dto.getCountry());
		brand.setLogoUrl(dto.getLogoUrl());
		brand.setWebsite(dto.getWebsite());

		boolean saveStatus = brandDAO.save(brand);

		return saveStatus ? new ApiResponse(true, "brand details saved successfully")
				: new ApiResponse(false, "some problem occurs while saving brand details");
	}

	public ApiResponse updateBrandDetails(BrandRequestDTO dto) {

		Brand brand = brandDAO.findBrandBySlug(dto.getSlug());

		if (brand == null)
			return new ApiResponse(false, "brand not avilable");

		brand = brandDAO.findBrandById(dto.getId());

		if (brand == null)
			return new ApiResponse(false, "brand not avilable");

		brand.setName(dto.getName());
		brand.setCountry(dto.getCountry());
		brand.setDescription(dto.getDescription());
		brand.setLogoUrl(dto.getLogoUrl());
		brand.setSlug(dto.getSlug());
		brand.setWebsite(dto.getWebsite());

		boolean updateStatus = brandDAO.update(brand);

		return updateStatus ? new ApiResponse(true, "brand details updated successfully")
				: new ApiResponse(false, "some problem occurs while updating brand details");
	}

	public ApiResponse deactivateBrand(Long id) {

		Brand brand = brandDAO.findBrandById(id);

		if (brand == null)
			return new ApiResponse(false, "Brand not available.");

		brand.setActive(false);

		boolean updateStatus = brandDAO.update(brand);

		return updateStatus ? new ApiResponse(true, "brand details deleted successfully")
				: new ApiResponse(false, "some problem occurs while deleting brand details");
	}

	public List<Brand> getAllBrandDetails() {

		List<Brand> brands = brandDAO.findAll();

		if (brands == null) {
			return List.of();
		}

		return brands;
	}

}
