package service;

import java.util.ArrayList;
import java.util.List;

import dao.CategoryDAO;
import dto.ApiResponse;
import dto.CategoryRequestDTO;
import dto.CategoryResponseDTO;
import entity.Category;

public class CategoryService {

	CategoryDAO categoryDAO;

	public CategoryService() {
		categoryDAO = new CategoryDAO();
	}

	public ApiResponse createCategory(CategoryRequestDTO dto) {

		Category category = categoryDAO.findByName(dto.getName());

		if (category != null)
			return new ApiResponse(false, "category already exists.");

		category = new Category();

		category.setName(dto.getName());
		category.setDescription(dto.getDescription());

		if (dto.getParentCategoryId() != null) {
			Category parent = categoryDAO.findById(dto.getParentCategoryId());

			if (parent == null)
				return new ApiResponse(false, "parent category not found.");

			category.setParentCategory(parent);
		}

		boolean saveStatus = categoryDAO.save(category);

		if (saveStatus)
			return new ApiResponse(true, "category added successfully.");
		else
			return new ApiResponse(false, "category failed to add");

	}

	public ApiResponse updateCategory(CategoryRequestDTO dto) {

		Category category = categoryDAO.findById(dto.getId());

		if (category == null)
			return new ApiResponse(false, "category not found.");

		Category existingCategory = categoryDAO.findByName(dto.getName());

		if (existingCategory != null && !existingCategory.getId().equals(category.getId()))
			return new ApiResponse(false, "category already exists.");

		category.setName(dto.getName());

		category.setDescription(dto.getDescription());

		if (dto.getParentCategoryId() == null) {
			category.setParentCategory(null);
		} else {
			Category parent = categoryDAO.findById(dto.getParentCategoryId());

			if (parent == null)
				return new ApiResponse(false, "parent category not found.");

			if (parent.getId().equals(category.getId()))
				return new ApiResponse(false, "parent category cannot be same as sub category.");

			if (parent.getId().equals(category.getParentCategory().getId()))
				return new ApiResponse(false, "already same parent category.");

			category.setParentCategory(parent);

		}

		categoryDAO.update(category);

		return new ApiResponse(true, "category updated successfully.");

	}

	public ApiResponse getAllCategories() {

		List<Category> categoryDetails = categoryDAO.findAll();
		
		List<CategoryResponseDTO> categoryList = new ArrayList<>();
		
		for(Category category : categoryDetails) {
			
			CategoryResponseDTO dto = new CategoryResponseDTO();
			
			dto.setName(category.getName());
			dto.setDescription(category.getDescription());
			dto.setId(category.getId());
			
			if(category.getParentCategory() != null) {
				dto.setParentCategoryId(category.getParentCategory().getId());	
				dto.setParentCategoryName(category.getParentCategory().getName());
			}
			
			categoryList.add(dto);
		}

		return new ApiResponse(true, "Fetched successfully", categoryList);
	}

	public ApiResponse deactivateCategory(Long id) {

		Category category = categoryDAO.findById(id);

		if (category == null)
			return new ApiResponse(false, "category not found.");

		category.setActive(false);

		boolean updateStatus = categoryDAO.update(category);

		if (updateStatus)
			return new ApiResponse(true, "category deleted");
		else
			return new ApiResponse(false, "failed to delete category");

	}

}
