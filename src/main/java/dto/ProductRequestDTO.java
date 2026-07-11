package dto;

public class ProductRequestDTO {
	
	private Long id;
	private String name;
	private String description;
	private Long brandId;
	private Long categoryId;
	private String slug;
	
	public ProductRequestDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public ProductRequestDTO(Long id, String name, String description, Long brand, Long category, String slug) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.brandId = brand;
		this.categoryId = category;
		this.slug = slug;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brand) {
		this.brandId = brand;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	@Override
	public String toString() {
		return "ProductRequestDTO [id=" + id + ", name=" + name + ", description=" + description + ", brand=" + brandId
				+ ", category=" + categoryId + ", slug=" + slug + "]";
	}

}
