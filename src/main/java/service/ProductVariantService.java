package service;

import java.util.List;

import dao.ProductDAO;
import dao.ProductVariantDAO;
import dto.ApiResponse;
import dto.ProductVariantRequestDTO;
import entity.Product;
import entity.ProductVariant;

public class ProductVariantService {

	ProductVariantDAO productVariantDAO;
	ProductDAO productDAO;

	public ProductVariantService() {

		productVariantDAO = new ProductVariantDAO();
		productDAO = new ProductDAO();
	}

	public ApiResponse addProductVariant(ProductVariantRequestDTO dto) {

		Product product = productDAO.findById(dto.getProductId());

		if (product == null)
			return new ApiResponse(false, "Product not found.");

		ProductVariant existingVariant = productVariantDAO.findBySku(dto.getSku());

		if (existingVariant != null)
			return new ApiResponse(false, "SKU already exists.");

		ProductVariant productVariant = new ProductVariant();

		productVariant.setProduct(product);
		productVariant.setSku(dto.getSku());
		productVariant.setPrice(dto.getPrice());
		productVariant.setDiscountPercentage(dto.getDiscountPercentage());
		productVariant.setStock(dto.getStock());

		boolean saveStatus = productVariantDAO.save(productVariant);

		return saveStatus ? new ApiResponse(true, "Product variant added successfully.")
				: new ApiResponse(false, "Unable to save product variant.");
	}

	public ApiResponse updateProductVariant(ProductVariantRequestDTO dto) {

		ProductVariant productVariant = productVariantDAO.findById(dto.getId());

		if (productVariant == null)
			return new ApiResponse(false, "Product variant not found.");

		productVariant.setPrice(dto.getPrice());
		productVariant.setDiscountPercentage(dto.getDiscountPercentage());
		productVariant.setStock(dto.getStock());

		boolean updateStatus = productVariantDAO.update(productVariant);

		return updateStatus ? new ApiResponse(true, "Product variant updated successfully.")
				: new ApiResponse(false, "Unable to update product variant.");
	}

	public List<ProductVariant> getAllVariants() {

		List<ProductVariant> variants = productVariantDAO.findAll();

		return variants == null ? List.of() : variants;
	}

	public ApiResponse deactivateVariant(Long variantId) {

		ProductVariant productVariant = productVariantDAO.findById(variantId);

		if (productVariant == null)
			return new ApiResponse(false, "Product variant not found.");

		productVariant.setActive(false);

		boolean updateStatus = productVariantDAO.update(productVariant);

		return updateStatus ? new ApiResponse(true, "Variant deactivated successfully.")
				: new ApiResponse(false, "Unable to deactivate variant.");
	}
}