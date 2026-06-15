package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import entity.Order;
import entity.User;
import util.HibernateUtil;

public class OrderDAO {

	public boolean save(Order order) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();

		try {

			session.persist(order);

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

	public boolean update(Order order) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();

		try {

			session.merge(order);

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

	public Order findById(Long id) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Order order = session.find(Order.class, id);

		session.close();

		return order;
	}

	public Order findByOrderNumber(String orderNumber) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		String hql = "FROM Order WHERE orderNumber = :orderNumber";

		Query<Order> query = session.createQuery(hql, Order.class);

		query.setParameter("orderNumber", orderNumber);

		Order order = query.uniqueResult();

		session.close();

		return order;
	}

	public List<Order> findByUser(User user) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		String hql = "FROM Order WHERE user = :user ORDER BY createdAt DESC";

		Query<Order> query = session.createQuery(hql, Order.class);

		query.setParameter("user", user);

		List<Order> orders = query.getResultList();

		session.close();

		return orders;
	}
}