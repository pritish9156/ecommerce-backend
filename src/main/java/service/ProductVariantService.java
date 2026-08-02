package service;

import java.util.ArrayList;
import java.util.List;

import dao.ProductDAO;
import dao.ProductImageDAO;
import dao.ProductVariantDAO;
import dto.request.ProductVariantRequestDTO;
import dto.response.ApiResponse;
import dto.response.ProductVariantResponseDTO;
import entity.Product;
import entity.ProductImage;
import entity.ProductVariant;

public class ProductVariantService {

	ProductVariantDAO productVariantDAO;
	ProductDAO productDAO;
	ProductImageDAO imageDAO;

	public ProductVariantService() {

		productVariantDAO = new ProductVariantDAO();
		productDAO = new ProductDAO();
		imageDAO = new ProductImageDAO();
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

	public List<ProductVariantResponseDTO> getAllVariants() {

		List<ProductVariant> variants = productVariantDAO.findAll();

		List<ProductVariantResponseDTO> variantDTOs = new ArrayList<>();

		for (ProductVariant variant : variants) {

			ProductVariantResponseDTO dto = new ProductVariantResponseDTO();

			dto.setId(variant.getId());

			dto.setProductId(variant.getProduct().getId());

			dto.setProductName(variant.getProduct().getName());

			dto.setSku(variant.getSku());

			dto.setPrice(variant.getPrice());

			dto.setDiscountPercentage(variant.getDiscountPercentage());

			dto.setStock(variant.getStock());

			dto.setActive(variant.isActive());

			variantDTOs.add(dto);
		}

		return variantDTOs;
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

	public ProductVariantResponseDTO getVariant(Long id) {

		 ProductVariant productVariant = productVariantDAO.findById(id);
		 
		 ProductVariantResponseDTO dto = new ProductVariantResponseDTO();
		 
		 dto.setActive(productVariant.isActive());
		 dto.setProductName(productVariant.getProduct().getName());
		 dto.setDiscountPercentage(productVariant.getDiscountPercentage());
		 dto.setId(productVariant.getId());
		 dto.setPrice(productVariant.getPrice());
		 dto.setProductId(productVariant.getProduct().getId());
		 dto.setStock(productVariant.getStock());
		 dto.setSku(productVariant.getSku());
		 
		 Product product = productVariant.getProduct();
		 
		 ProductImage image = imageDAO.findFirstImageByProduct(product);
		 
		 dto.setImage(image);
		 
		 return dto;
	}
}