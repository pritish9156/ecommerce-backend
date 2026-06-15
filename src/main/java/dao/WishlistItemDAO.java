package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import entity.ProductVariant;
import entity.User;
import entity.Wishlist;
import util.HibernateUtil;

public class WishlistItemDAO {

	public boolean save(Wishlist wishlistItem) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		try {

			session.persist(wishlistItem);

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

	public boolean delete(Wishlist wishlistItem) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		try {

			Wishlist managedWishlistItem = session.merge(wishlistItem);

			session.remove(managedWishlistItem);

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

	public Wishlist findById(Long id) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Wishlist wishlistItem = session.find(Wishlist.class, id);

		session.close();

		return wishlistItem;
	}

	public Wishlist findByUserAndVariant(User user, ProductVariant variant) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		String hql = "FROM WishlistItem WHERE user = :user AND productVariant = :variant";

		Query<Wishlist> query = session.createQuery(hql, Wishlist.class);

		query.setParameter("user", user);
		query.setParameter("variant", variant);

		Wishlist wishlistItem = query.uniqueResult();

		session.close();

		return wishlistItem;
	}

	public List<Wishlist> findByUser(User user) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		String hql = "FROM WishlistItem WHERE user = :user ORDER BY createdAt DESC";

		Query<Wishlist> query = session.createQuery(hql, Wishlist.class);

		query.setParameter("user", user);

		List<Wishlist> wishlistItems = query.getResultList();

		session.close();

		return wishlistItems;
	}
}