package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
		name = "variant_attributes",
		uniqueConstraints = {
		@UniqueConstraint(
			    columnNames = {
			        "product_variant_id",
			        "attribute_id"
			    }
			)})
public class VariantAttribute {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "product_variant_id", nullable = false)
	private ProductVariant productVariant;

	@ManyToOne
	@JoinColumn(name = "attribute_id", nullable = false)
	private Attribute attribute;

	@ManyToOne
	@JoinColumn(name = "attribute_value_id", nullable = false)
	private AttributeValue attributeValue;

	public VariantAttribute(ProductVariant productVariant, Attribute attribute, AttributeValue attributeValue) {
		super();
		this.productVariant = productVariant;
		this.attribute = attribute;
		this.attributeValue = attributeValue;
	}
	
	public VariantAttribute() {
		// TODO Auto-generated constructor stub
	}

	public ProductVariant getProductVariant() {
		return productVariant;
	}

	public void setProductVariant(ProductVariant productVariant) {
		this.productVariant = productVariant;
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	public AttributeValue getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(AttributeValue attributeValue) {
		this.attributeValue = attributeValue;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "VariantAttribute [id=" + id + ", productVariant=" + productVariant + ", attribute=" + attribute
				+ ", attributeValue=" + attributeValue + "]";
	}
	
}
