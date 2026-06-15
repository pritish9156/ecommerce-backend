package entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "brands")
public class Brand {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "brand_id")
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String name;
	
	@Column(nullable = false, unique = true)
	private String slug;
	
	private String logoUrl;
	
	private String description;
	
	private String website;
	
	private String country;
	
	@Column(nullable = false)
	private boolean isActive;
	
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	@Column(nullable = false)
	private LocalDateTime updatedAt;
	
	@PrePersist
	public void preCreate() {
		isActive = true;
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}
	
	@PreUpdate
	public void preUpdate() {
		updatedAt = LocalDateTime.now();
	}

	public Brand(String name, String slug, String logoUrl, String description, String website, String country,
			boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.name = name;
		this.slug = slug;
		this.logoUrl = logoUrl;
		this.description = description;
		this.website = website;
		this.country = country;
		this.isActive = isActive;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	
	public Brand() {
	
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public long getId() {
		return id;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	@Override
	public String toString() {
		return "Brand [id=" + id + ", name=" + name + ", slug=" + slug + ", logoUrl=" + logoUrl + ", description="
				+ description + ", website=" + website + ", country=" + country + ", isActive=" + isActive
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
	
}
