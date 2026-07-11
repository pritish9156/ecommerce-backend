package dto;

import java.util.List;

import entity.Category;

public class CategoryRequestDTO {

	private Long id;
	
	private String name;
	
	private String description;
    
    private Long parentCategoryId;
    
    public CategoryRequestDTO() {

	}

	public CategoryRequestDTO(Long id, String name, String description, Long parentCategory, List<Category> subCategories) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.parentCategoryId = parentCategory;
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

	public Long getParentCategoryId() {
		return parentCategoryId;
	}

	public void setParentCategoryId(Long parentCategory) {
		this.parentCategoryId = parentCategory;
	}

	@Override
	public String toString() {
		return "CategoryRequestDTO [name=" + name + ", id=" + id + ", description=" + description + ", parentCategory="
				+ parentCategoryId + "]";
	}
    
}
