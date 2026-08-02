package dto.response;

public class CategoryResponseDTO {

    private Long id;

    private String name;

    private String description;

    private Long parentCategoryId;
    
    private String parentCategoryName;
    
    public CategoryResponseDTO() {
		
	}

	public CategoryResponseDTO(Long id, String name, String description, Long parentCategoryId,
			String parentCategoryName) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.parentCategoryId = parentCategoryId;
		this.parentCategoryName = parentCategoryName;
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

	public void setParentCategoryId(Long parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}
	
	public String getParentCategoryName() {
		return parentCategoryName;
	}
	
	public void setParentCategoryName(String parentCategoryName) {
		this.parentCategoryName = parentCategoryName;
	}
    
}