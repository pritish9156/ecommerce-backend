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

}
