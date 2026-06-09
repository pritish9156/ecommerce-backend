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
    name = "product_categories",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {
                "product_id",
                "category_id"
            }
        )
    }
)
public class ProductCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(
	    name = "product_id",
	    nullable = false
	)
	private Product product;
	
	@ManyToOne
	@JoinColumn(
	    name = "category_id",
	    nullable = false
	)
	private Category category;

	public ProductCategory(Product product, Category category) {
		super();
		this.product = product;
		this.category = category;
	}
	
	public ProductCategory() {
		// TODO Auto-generated constructor stub
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "ProductCategory [id=" + id + ", product=" + product + ", category=" + category + "]";
	}
	
}
