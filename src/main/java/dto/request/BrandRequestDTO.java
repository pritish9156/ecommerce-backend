package dto.request;

public class BrandRequestDTO {
	
	Long id;
	String name;
	String description;
	String slug;
	String logoUrl;
	String country;
	String website;
	
	public BrandRequestDTO() {
		
	}
	
	public BrandRequestDTO(Long id, String name, String description, String slug, String logoUrl, String country,
			String website) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.slug = slug;
		this.logoUrl = logoUrl;
		this.country = country;
		this.website = website;
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

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	@Override
	public String toString() {
		return "BrandRequestDTO [name=" + name + ", description=" + description + ", slug=" + slug + ", logoUrl="
				+ logoUrl + ", country=" + country + ", website=" + website + "]";
	}
	
	

}
