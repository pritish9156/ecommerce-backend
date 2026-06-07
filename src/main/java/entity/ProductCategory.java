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
}
