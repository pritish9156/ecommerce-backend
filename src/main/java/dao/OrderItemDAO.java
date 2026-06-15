package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import entity.Order;
import entity.OrderItem;
import entity.ProductVariant;
import entity.User;
import util.HibernateUtil;

public class OrderItemDAO {

	public boolean save(OrderItem orderItem) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();

		try {

			session.persist(orderItem);

			tx.commit();

			return true;

		} catch (Exception e) {

			tx.rollback();
			e.printStackTrace();

		} finally {

			session.close();
		}

		return false;
	}

	public OrderItem findById(Long id) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		OrderItem orderItem = session.find(OrderItem.class, id);

		session.close();

		return orderItem;
	}

	public List<OrderItem> findByOrder(Order order) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		String hql = "FROM OrderItem WHERE order = :order";

		Query<OrderItem> query = session.createQuery(hql, OrderItem.class);

		query.setParameter("order", order);

		List<OrderItem> orderItems = query.getResultList();

		session.close();

		return orderItems;
	}

	public boolean hasPurchasedVariant(User user, ProductVariant variant) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		String hql = """
				SELECT COUNT(oi)
				FROM OrderItem oi
				WHERE oi.order.user = :user
				AND oi.productVariant = :variant
				""";

		Query<Long> query = session.createQuery(hql, Long.class);

		query.setParameter("user", user);

		query.setParameter("variant", variant);

		Long count = query.uniqueResult();

		session.close();

		return count != null && count > 0;
	}
}