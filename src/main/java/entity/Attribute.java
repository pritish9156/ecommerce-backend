package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "attributes")
public class Attribute {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "attribute_id")
	private long id;
	
	@Column(nullable = false, unique = true)
	private String name;
	
	private String unit;
	
	private String description;
	
	private boolean isFilterable;
	
	private boolean isActive;
	
	@PrePersist
	public void preCreate() {
		isActive = true;
	}
	
	public Attribute() {
	
	}

	public Attribute(String name, String unit, String description, boolean isFilterable, boolean isActive) {
		super();
		this.name = name;
		this.unit = unit;
		this.description = description;
		this.isFilterable = isFilterable;
		this.isActive = isActive;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isFilterable() {
		return isFilterable;
	}

	public void setFilterable(boolean isFilterable) {
		this.isFilterable = isFilterable;
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

	@Override
	public String toString() {
		return "Attribute [id=" + id + ", name=" + name + ", unit=" + unit + ", description=" + description
				+ ", isFilterable=" + isFilterable + ", isActive=" + isActive + "]";
	}

}
