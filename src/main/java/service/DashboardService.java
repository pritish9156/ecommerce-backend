package service;

import java.util.ArrayList;
import java.util.List;

import dao.DashboardDAO;
import dto.AdminDashboardDTO;
import dto.LowStockProductDTO;
import dto.RecentOrderDTO;
import entity.Order;
import entity.ProductVariant;

public class DashboardService {

	private DashboardDAO dashboardDAO;

	public DashboardService() {

		dashboardDAO = new DashboardDAO();
	}

	public AdminDashboardDTO getDashboardStats() {

		AdminDashboardDTO dto = new AdminDashboardDTO();

		dto.setTotalUsers(dashboardDAO.getTotalUsers());

		dto.setTotalProducts(dashboardDAO.getTotalProducts());

		dto.setTotalOrders(dashboardDAO.getTotalOrders());

		dto.setTotalRevenue(dashboardDAO.getTotalRevenue());

		return dto;
	}

	public List<LowStockProductDTO> getLowStockProducts() {

		List<ProductVariant> variants = dashboardDAO.getLowStockProducts();

		List<LowStockProductDTO> result = new ArrayList<>();

		for (ProductVariant variant : variants) {

			LowStockProductDTO dto = new LowStockProductDTO();

			dto.setProductId(variant.getProduct().getId());

			dto.setProductName(variant.getProduct().getName());

			dto.setSku(variant.getSku());

			dto.setStock(variant.getStock());

			result.add(dto);
		}

		return result;
	}

	public List<RecentOrderDTO> getRecentOrders() {

		List<Order> orders = dashboardDAO.getRecentOrders();

		List<RecentOrderDTO> result = new ArrayList<>();

		for (Order order : orders) {

			RecentOrderDTO dto = new RecentOrderDTO();

			dto.setOrderNumber(order.getOrderNumber());

			dto.setCustomerName(order.getUser().getFirstName() + " " + order.getUser().getLastName());

			dto.setTotalAmount(order.getTotalAmount());

			dto.setOrderStatus(order.getOrderStatus());

			result.add(dto);
		}

		return result;
	}
}