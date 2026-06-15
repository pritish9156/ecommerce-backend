package service;

import java.util.List;

import dao.ProductDAO;
import dao.ProductImageDAO;
import dto.ApiResponse;
import dto.ProductImageRequestDTO;
import entity.Product;
import entity.ProductImage;

public class ProductImageService {

	private ProductDAO productDAO;
	private ProductImageDAO productImageDAO;

	public ProductImageService() {

		productDAO = new ProductDAO();
		productImageDAO = new ProductImageDAO();
	}

	public ApiResponse addProductImage(ProductImageRequestDTO dto) {

		Product product = productDAO.findById(dto.getProductId());

		if (product == null)
			return new ApiResponse(false, "Product not found.");

		ProductImage image = new ProductImage();

		image.setProduct(product);
		image.setImageUrl(dto.getImageUrl());
		image.setDisplayOrder(dto.getDisplayOrder());
		image.setAltText(dto.getAltText());

		boolean status = productImageDAO.save(image);

		return status ? new ApiResponse(true, "Image added successfully.")
				: new ApiResponse(false, "Unable to add image.");
	}

	public ApiResponse updateProductImage(ProductImageRequestDTO dto) {

		ProductImage image = productImageDAO.findById(dto.getId());

		if (image == null)
			return new ApiResponse(false, "Image not found.");

		image.setImageUrl(dto.getImageUrl());
		image.setDisplayOrder(dto.getDisplayOrder());
		image.setAltText(dto.getAltText());

		boolean status = productImageDAO.update(image);

		return status ? new ApiResponse(true, "Image updated successfully.")
				: new ApiResponse(false, "Unable to update image.");
	}

	public ApiResponse deleteProductImage(Long imageId) {

		ProductImage image = productImageDAO.findById(imageId);

		if (image == null)
			return new ApiResponse(false, "Image not found.");

		boolean status = productImageDAO.delete(image);

		return status ? new ApiResponse(true, "Image deleted successfully.")
				: new ApiResponse(false, "Unable to delete image.");
	}

	public List<ProductImage> getImagesByProduct(Long productId) {

		Product product = productDAO.findById(productId);

		if (product == null)
			return List.of();

		return productImageDAO.findByProduct(product);
	}
}