package dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import entity.Payment;
import util.HibernateUtil;

public class PaymentDAO {

	public boolean save(Payment payment) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();

		try {

			session.persist(payment);

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

	public boolean update(Payment payment) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();

		try {

			session.merge(payment);

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

	public Payment findById(Long id) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Payment payment = session.find(Payment.class, id);

		session.close();

		return payment;
	}

	public Payment findByOrder(entity.Order order) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Payment payment = session.createQuery("FROM Payment WHERE order = :order", Payment.class)
				.setParameter("order", order).uniqueResult();

		session.close();

		return payment;
	}

	public Payment findByOrderId(Long orderId) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Payment payment = session.createQuery("FROM Payment WHERE order.id = :orderId", Payment.class)
				.setParameter("orderId", orderId).uniqueResult();

		session.close();

		return payment;
	}

	public Payment findByRazorpayOrderId(String razorpayOrderId) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		String hql = "FROM Payment WHERE razorpayOrderId = :id";

		Query<Payment> query = session.createQuery(hql, Payment.class);

		query.setParameter("id", razorpayOrderId);

		Payment payment = query.uniqueResult();

		session.close();

		return payment;
	}
}