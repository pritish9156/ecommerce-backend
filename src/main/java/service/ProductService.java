package service;

import java.util.List;

import dao.BrandDAO;
import dao.ProductDAO;
import dao.ProductImageDAO;
import dao.ProductVariantDAO;
import dao.ReviewDAO;
import dto.ApiResponse;
import dto.ProductDetailsResponseDTO;
import dto.ProductRequestDTO;
import dto.ProductSearchRequestDTO;
import dto.ProductSearchResponseDTO;
import entity.Brand;
import entity.Product;
import entity.ProductImage;
import entity.ProductVariant;
import entity.Review;

public class ProductService {

	private ProductDAO productDAO;
	private BrandDAO brandDAO;
	private ProductVariantDAO productVariantDAO;
	private ProductImageDAO productImageDAO;
	private ReviewDAO reviewDAO;

	public ProductService() {

		productDAO = new ProductDAO();
		brandDAO = new BrandDAO();
		productVariantDAO = new ProductVariantDAO();
		productImageDAO = new ProductImageDAO();
		reviewDAO = new ReviewDAO();

	}

	public ApiResponse addProduct(ProductRequestDTO dto) {

		Product existingProduct = productDAO.findByName(dto.getName());

		if (existingProduct != null) {

			return new ApiResponse(false, "Product already exists.");
		}

		Product existingSlug = productDAO.findBySlug(dto.getSlug());

		if (existingSlug != null) {

			return new ApiResponse(false, "Product slug already exists.");
		}

		Brand brand = brandDAO.findBrandById(dto.getBrandId());

		if (brand == null) {

			return new ApiResponse(false, "Brand not found.");
		}

		Product product = new Product();

		product.setName(dto.getName());
		product.setDescription(dto.getDescription());
		product.setSlug(dto.getSlug());
		product.setBrand(brand);

		boolean saveStatus = productDAO.save(product);

		return saveStatus ? new ApiResponse(true, "Product saved successfully.")
				: new ApiResponse(false, "Unable to save product.");
	}

	public ApiResponse updateProduct(ProductRequestDTO dto) {

		Product product = productDAO.findById(dto.getId());

		if (product == null) {

			return new ApiResponse(false, "Product not found.");
		}

		Brand brand = brandDAO.findBrandById(dto.getBrandId());

		if (brand == null) {

			return new ApiResponse(false, "Brand not found.");
		}

		product.setName(dto.getName());
		product.setDescription(dto.getDescription());
		product.setSlug(dto.getSlug());
		product.setBrand(brand);

		boolean updateStatus = productDAO.update(product);

		return updateStatus ? new ApiResponse(true, "Product updated successfully.")
				: new ApiResponse(false, "Unable to update product.");
	}

	public ApiResponse deactivateProduct(Long productId) {

		Product product = productDAO.findById(productId);

		if (product == null) {

			return new ApiResponse(false, "Product not found.");
		}

		product.setActive(false);

		boolean updateStatus = productDAO.update(product);

		return updateStatus ? new ApiResponse(true, "Product deactivated successfully.")
				: new ApiResponse(false, "Unable to deactivate product.");
	}

	public List<Product> getAllProducts() {

		List<Product> products = productDAO.findAll();

		if (products == null) {
			return List.of();
		}

		return products;
	}

	public ProductSearchResponseDTO searchProducts(ProductSearchRequestDTO dto) {

		int page = dto.getPage() == null ? 0 : dto.getPage();

		int size = dto.getSize() == null ? 10 : dto.getSize();

		List<Product> products = productDAO.searchProducts(dto.getKeyword(), dto.getBrandId(), page, size);

		Long totalRecords = productDAO.countProducts(dto.getKeyword(), dto.getBrandId(), dto.getSortBy(),
				dto.getSortDirection());

		int totalPages = (int) Math.ceil((double) totalRecords / size);

		ProductSearchResponseDTO response = new ProductSearchResponseDTO();

		response.setProducts(products);
		response.setTotalRecords(totalRecords);
		response.setTotalPages(totalPages);
		response.setCurrentPage(page);

		return response;
	}

	public ProductDetailsResponseDTO getProductDetails(Long productId) {

		Product product = productDAO.findById(productId);

		if (product == null)
			return null;

		List<ProductVariant> variants = productVariantDAO.findByProduct(product);

		List<ProductImage> images = productImageDAO.findByProduct(product);

		List<Review> reviews = reviewDAO.findByProduct(product);

		ProductDetailsResponseDTO response = new ProductDetailsResponseDTO();

		response.setProduct(product);
		response.setVariants(variants);
		response.setImages(images);
		response.setReviews(reviews);

		return response;
	}
}