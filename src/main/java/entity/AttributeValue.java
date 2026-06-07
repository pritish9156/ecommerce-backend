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
	
}
