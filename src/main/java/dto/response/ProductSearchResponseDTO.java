package dto.response;

import java.util.List;

import entity.Product;

public class ProductSearchResponseDTO {

	private List<ProductCardResponseDTO> products;
    private Long totalRecords;
    private Integer totalPages;
    private Integer currentPage;

    public ProductSearchResponseDTO() {
    }

    public List<ProductCardResponseDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductCardResponseDTO> products) {
        this.products = products;
    }

    public Long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }
}