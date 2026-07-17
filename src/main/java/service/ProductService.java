package service;

import java.util.ArrayList;
import java.util.List;

import dao.BrandDAO;
import dao.CategoryDAO;
import dao.ProductDAO;
import dao.ProductImageDAO;
import dao.ProductVariantDAO;
import dao.ReviewDAO;
import dao.TagDAO;
import dto.ApiResponse;
import dto.ProductCardResponseDTO;
import dto.ProductDetailsResponseDTO;
import dto.ProductRequestDTO;
import dto.ProductSearchRequestDTO;
import dto.ProductSearchResponseDTO;
import entity.Brand;
import entity.Category;
import entity.Product;
import entity.ProductImage;
import entity.ProductVariant;
import entity.Review;
import entity.Tags;

public class ProductService {

	private ProductDAO productDAO;
	private BrandDAO brandDAO;
	private CategoryDAO categoryDAO;
	private TagDAO tagDAO;
	private ProductVariantDAO productVariantDAO;
	private ProductImageDAO productImageDAO;
	private ReviewDAO reviewDAO;

	public ProductService() {

		productDAO = new ProductDAO();
		brandDAO = new BrandDAO();
		categoryDAO = new CategoryDAO();
		productVariantDAO = new ProductVariantDAO();
		productImageDAO = new ProductImageDAO();
		reviewDAO = new ReviewDAO();
		tagDAO = new TagDAO();

	}

	private String generateSlug(String text) {

		return text.toLowerCase().trim().replaceAll("\\s+", "-").replaceAll("[^a-z0-9-]", "");
	}

	public ApiResponse addProduct(ProductRequestDTO dto) {

		Product existingProduct = productDAO.findByName(dto.getName());

		if (existingProduct != null) {

			return new ApiResponse(false, "Product already exists.");
		}

		String slug = generateSlug(dto.getName());

		Product existingSlug = productDAO.findBySlug(slug);

		if (existingSlug != null) {
			return new ApiResponse(false, "Product slug already exists.");
		}

		Brand brand = brandDAO.findBrandById(dto.getBrandId());

		if (brand == null) {

			return new ApiResponse(false, "Brand not found.");
		}

		Category category = categoryDAO.findById(dto.getCategoryId());

		if (category == null) {
			return new ApiResponse(false, "Category not found.");
		}

		List<Tags> tags = new ArrayList<Tags>();

		if (dto.getTagIds() != null) {

			for (Long tagId : dto.getTagIds()) {

				Tags tag = tagDAO.findById(tagId);

				if (tag == null)
					return new ApiResponse(false, "Tag not found with ID: " + tagId);

				tags.add(tag);
			}

		}

		Product product = new Product();

		product.setName(dto.getName());
		product.setDescription(dto.getDescription());
		product.setSlug(slug);
		product.setBrand(brand);
		product.setCategory(category);
		product.setTags(tags);

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

		Category category = categoryDAO.findById(dto.getCategoryId());

		if (category == null) {

			return new ApiResponse(false, "Category not found.");
		}

		List<Tags> tags = new ArrayList<Tags>();

		if (dto.getTagIds() != null) {

			for (Long tagId : dto.getTagIds()) {

				Tags tag = tagDAO.findById(tagId);

				if (tag == null)
					return new ApiResponse(false, "Tag not found with ID: " + tagId);

				tags.add(tag);
			}

		}

		product.setName(dto.getName());
		product.setDescription(dto.getDescription());
		product.setSlug(dto.getSlug());
		product.setBrand(brand);
		product.setCategory(category);
		product.setTags(tags);

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

		List<ProductCardResponseDTO> productCards = new ArrayList<>();

		for (Product product : products) {

			ProductCardResponseDTO card = new ProductCardResponseDTO();

			card.setId(product.getId());

			card.setName(product.getName());

			card.setBrandName(product.getBrand().getName());

			card.setAverageRating(product.getAverageRating());

			card.setReviewCount(product.getReviewCount());

			card.setSlug(product.getSlug());

			ProductImage image = productImageDAO.findFirstImageByProduct(product);

			if (image != null) {

				card.setImageUrl(image.getImageUrl());
			}

			ProductVariant variant = productVariantDAO.findLowestPriceVariant(product);

			if (variant != null) {

				card.setStartingPrice(variant.getPrice().doubleValue());
			}

			productCards.add(card);
		}

		Long totalRecords = productDAO.countProducts(dto.getKeyword(), dto.getBrandId(), dto.getSortBy(),
				dto.getSortDirection());

		int totalPages = (int) Math.ceil((double) totalRecords / size);

		ProductSearchResponseDTO response = new ProductSearchResponseDTO();

		response.setProducts(productCards);

		response.setTotalRecords(totalRecords);

		response.setTotalPages(totalPages);

		response.setCurrentPage(page);

		return response;
	}

	public ProductDetailsResponseDTO getProductDetails(Long productId) {

		Product product = productDAO.findById(productId);

		if (product == null)
			return null;

		ProductDetailsResponseDTO dto = new ProductDetailsResponseDTO();

		dto.setId(product.getId());

		dto.setName(product.getName());

		dto.setDescription(product.getDescription());

		dto.setBrandName(product.getBrand().getName());

		dto.setAverageRating(product.getAverageRating());

		dto.setReviewCount(product.getReviewCount());

		if (product.getCategory() != null) {

			dto.setCategoryId(product.getCategory().getId());

			dto.setCategoryName(product.getCategory().getName());
		}

		List<Tags> tags = tagDAO.findByProductId(product.getId());

		List<Long> tagIds = new ArrayList<>();

		List<String> tagNames = new ArrayList<>();

		for (Tags tag : tags) {

		    tagIds.add(tag.getId());

		    tagNames.add(tag.getName());
		}

		dto.setTagIds(tagIds);

		dto.setTagNames(tagNames);
		
		dto.setImages(productImageDAO.findByProduct(product));

		dto.setVariants(productVariantDAO.findByProduct(product));

		return dto;
	}
	
	public ApiResponse getRelatedProducts(Long productId) {
		
		Product product = productDAO.findById(productId);
		
		if(product == null)
			return new ApiResponse(false, "product not found.");
		
		Long categoryId = product.getCategory().getId();
		
		Category category = categoryDAO.findById(categoryId);
		
		if(category == null)
			return new ApiResponse(false, "category not found.");
		
		List<Product> relatedProducts = productDAO.findRelatedProducts(categoryId, productId);

		List<ProductCardResponseDTO> productCards = new ArrayList<ProductCardResponseDTO>();
		
		for(Product p : relatedProducts) {
			
			ProductCardResponseDTO card = new ProductCardResponseDTO();

			card.setId(p.getId());

			card.setName(p.getName());

			card.setBrandName(p.getBrand().getName());

			card.setAverageRating(p.getAverageRating());

			card.setReviewCount(p.getReviewCount());

			card.setSlug(p.getSlug());

			ProductImage image = productImageDAO.findFirstImageByProduct(p);

			if (image != null) {

				card.setImageUrl(image.getImageUrl());
			}

			ProductVariant variant = productVariantDAO.findLowestPriceVariant(p);

			if (variant != null) {

				card.setStartingPrice(variant.getPrice().doubleValue());
			}
			
			productCards.add(card);
			
		}
		
		if(productCards.isEmpty())
			return new ApiResponse(true, "No related products found.", productCards);
		else
			return new ApiResponse(true, "related products fetched successfully.", productCards);
	}

}