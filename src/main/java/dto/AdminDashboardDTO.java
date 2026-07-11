package dto;

import java.math.BigDecimal;

public class AdminDashboardDTO {

    private Long totalUsers;
    private Long totalProducts;
    private Long totalOrders;
    private BigDecimal totalRevenue;
    private Long pendingOrders;
    private Long lowStockProducts;
    
    
    public AdminDashboardDTO() {
		// TODO Auto-generated constructor stub
	}
    
	public AdminDashboardDTO(Long totalUsers, Long totalProducts, Long totalOrders, BigDecimal totalRevenue,
			Long pendingOrders, Long lowStockProducts) {
		super();
		this.totalUsers = totalUsers;
		this.totalProducts = totalProducts;
		this.totalOrders = totalOrders;
		this.totalRevenue = totalRevenue;
		this.pendingOrders = pendingOrders;
		this.lowStockProducts = lowStockProducts;
	}
	public Long getTotalUsers() {
		return totalUsers;
	}
	public void setTotalUsers(Long totalUsers) {
		this.totalUsers = totalUsers;
	}
	public Long getTotalProducts() {
		return totalProducts;
	}
	public void setTotalProducts(Long totalProducts) {
		this.totalProducts = totalProducts;
	}
	public Long getTotalOrders() {
		return totalOrders;
	}
	public void setTotalOrders(Long totalOrders) {
		this.totalOrders = totalOrders;
	}
	public BigDecimal getTotalRevenue() {
		return totalRevenue;
	}
	public void setTotalRevenue(BigDecimal totalRevenue) {
		this.totalRevenue = totalRevenue;
	}
	public Long getPendingOrders() {
		return pendingOrders;
	}
	public void setPendingOrders(Long pendingOrders) {
		this.pendingOrders = pendingOrders;
	}
	public Long getLowStockProducts() {
		return lowStockProducts;
	}
	public void setLowStockProducts(Long lowStockProducts) {
		this.lowStockProducts = lowStockProducts;
	}
    
    

}