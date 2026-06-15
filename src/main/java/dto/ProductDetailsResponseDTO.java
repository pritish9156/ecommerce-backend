package dto;

import java.util.List;

import entity.Product;
import entity.ProductImage;
import entity.ProductVariant;
import entity.Review;

public class ProductDetailsResponseDTO {

    private Product product;
    private List<ProductVariant> variants;
    private List<ProductImage> images;
    private List<Review> reviews;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<ProductVariant> getVariants() {
        return variants;
    }

    public void setVariants(List<ProductVariant> variants) {
        this.variants = variants;
    }

    public List<ProductImage> getImages() {
        return images;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}