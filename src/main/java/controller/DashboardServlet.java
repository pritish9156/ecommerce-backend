package controller;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import dto.AdminDashboardDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.DashboardService;

@WebServlet("/admin/dashboard")
public class DashboardServlet extends HttpServlet {

	private DashboardService dashboardService;

	private ObjectMapper objectMapper;

	@Override
	public void init() {

		dashboardService = new DashboardService();

		objectMapper = new ObjectMapper();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String type = request.getParameter("type");

		if ("low-stock".equals(type)) {

			objectMapper.writeValue(

					response.getWriter(),

					dashboardService.getLowStockProducts());

			return;
		}

		if ("recent-orders".equals(type)) {

			objectMapper.writeValue(response.getWriter(), dashboardService.getRecentOrders());

			return;
		}

		AdminDashboardDTO dto = dashboardService.getDashboardStats();

		response.setContentType("application/json");

		objectMapper.writeValue(response.getWriter(), dto);
	}
}