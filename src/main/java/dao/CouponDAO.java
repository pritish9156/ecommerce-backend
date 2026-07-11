package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import entity.Coupon;
import util.HibernateUtil;

public class CouponDAO {

	public boolean save(Coupon coupon) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();

		try {

			session.persist(coupon);

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

	public Coupon findByCode(String code) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Coupon coupon = session.createQuery("FROM Coupon WHERE code=:code", Coupon.class).setParameter("code", code)
				.uniqueResult();

		session.close();

		return coupon;
	}

	public List<Coupon> findAll() {

		Session session = HibernateUtil.getSessionFactory().openSession();

		List<Coupon> coupons = session.createQuery("FROM Coupon", Coupon.class).getResultList();

		session.close();

		return coupons;
	}

	public Coupon findById(Long id) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Coupon coupon = session.find(Coupon.class, id);

		session.close();

		return coupon;
	}

	public boolean update(Coupon coupon) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();

		try {

			session.merge(coupon);

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

	public boolean delete(Coupon coupon) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();

		try {

			Coupon managed = session.merge(coupon);

			session.remove(managed);

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
}