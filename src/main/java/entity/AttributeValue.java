package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
	    name = "attribute_values",
	    uniqueConstraints = {
	        @UniqueConstraint(
	            columnNames = {"attribute_id", "value"} 
	        )
	    }
	)
public class AttributeValue {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	@JoinColumn(name = "attribute_id", nullable = false)
	private Attribute attribute;

	@Column(nullable = false)
	private String value;

	@Column(nullable = false)
	private boolean isActive;
	
	@PrePersist
	public void preCreate() {
		isActive = true;
	}

	public AttributeValue(Attribute attribute, String value, boolean isActive) {
		super();
		this.attribute = attribute;
		this.value = value;
		this.isActive = isActive;
	}
	
	public AttributeValue() {
		
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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
		return "AttributeValue [id=" + id + ", attribute=" + attribute + ", value=" + value + ", isActive=" + isActive
				+ "]";
	}
	
}
