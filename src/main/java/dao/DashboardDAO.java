package dao;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Session;

import entity.Order;
import entity.ProductVariant;
import util.HibernateUtil;

public class DashboardDAO {

	public Long getTotalUsers() {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Long count = session.createQuery("SELECT COUNT(u) FROM User u", Long.class).uniqueResult();

		session.close();

		return count;
	}

	public Long getTotalProducts() {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Long count = session.createQuery("SELECT COUNT(p) FROM Product p", Long.class).uniqueResult();

		session.close();

		return count;
	}

	public Long getTotalOrders() {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Long count = session.createQuery("SELECT COUNT(o) FROM Order o", Long.class).uniqueResult();

		session.close();

		return count;
	}

	public BigDecimal getTotalRevenue() {

		Session session = HibernateUtil.getSessionFactory().openSession();

		BigDecimal revenue = session.createQuery("SELECT COALESCE(SUM(o.totalAmount),0) FROM Order o", BigDecimal.class)
				.uniqueResult();

		session.close();

		return revenue;
	}

	public List<ProductVariant> getLowStockProducts() {

		Session session = HibernateUtil.getSessionFactory().openSession();

		List<ProductVariant> variants = session.createQuery("""
				FROM ProductVariant
				WHERE stock <= 10
				AND isActive = true
				ORDER BY stock ASC
				""", ProductVariant.class).getResultList();

		session.close();

		return variants;
	}

	public List<Order> getRecentOrders() {

		Session session = HibernateUtil.getSessionFactory().openSession();

		List<Order> orders = session.createQuery("""
				FROM Order
				ORDER BY createdAt DESC
				""", Order.class).setMaxResults(5).getResultList();

		session.close();

		return orders;
	}
}