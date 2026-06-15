package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;

import entity.Cart;
import entity.CartItem;
import entity.ProductVariant;
import util.HibernateUtil;

public class CartItemDAO {

	public boolean save(CartItem cartItem) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();

		try {
			session.persist(cartItem);
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

	public boolean update(CartItem cartItem) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();

		try {
			session.merge(cartItem);
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

	public boolean delete(CartItem cartItem) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();

		try {
			CartItem managedCartItem = session.merge(cartItem);
			session.remove(managedCartItem);
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

	public List<CartItem> findByCart(Cart cart) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		String hql = "FROM CartItem WHERE cart = :cart";

		Query<CartItem> query = session.createQuery(hql, CartItem.class);

		query.setParameter("cart", cart);

		try {
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return null;
	}

	public CartItem findByCartAndProductVariant(Cart cart, ProductVariant productVariant) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		String hql = "FROM CartItem WHERE cart = :cart and productVariant = :productVariant";

		Query<CartItem> query = session.createQuery(hql, CartItem.class);

		query.setParameter("cart", cart);
		query.setParameter("productVariant", productVariant);

		try {
			return query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return null;
	}

	public CartItem findById(Long id) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		CartItem cartItem = session.find(CartItem.class, id);

		session.close();

		return cartItem;
	}

	public boolean deleteAllByCart(Cart cart) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();

		String hql = "DELETE FROM CartItem WHERE cart = :cart";

		MutationQuery query = session.createMutationQuery(hql);

		query.setParameter("cart", cart);

		try {

			int result = query.executeUpdate();

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
