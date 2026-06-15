package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import entity.Product;
import entity.ProductVariant;
import entity.Review;
import entity.User;
import util.HibernateUtil;

public class ReviewDAO {

	public boolean save(Review review) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();

		try {

			session.persist(review);

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

	public boolean update(Review review) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();

		try {

			session.merge(review);

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

	public boolean delete(Review review) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();

		try {

			Review managedReview = session.merge(review);

			session.remove(managedReview);

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

	public Review findById(Long id) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Review review = session.find(Review.class, id);

		session.close();

		return review;
	}

	public Review findByUserAndVariant(User user, ProductVariant productVariant) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		String hql = "FROM Review WHERE user = :user AND productVariant = :productVariant";

		Query<Review> query = session.createQuery(hql, Review.class);

		query.setParameter("user", user);

		query.setParameter("productVariant", productVariant);

		Review review = query.uniqueResult();

		session.close();

		return review;
	}

	public List<Review> findByVariant(ProductVariant productVariant) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		String hql = "FROM Review WHERE productVariant = :productVariant";

		Query<Review> query = session.createQuery(hql, Review.class);

		query.setParameter("productVariant", productVariant);

		List<Review> reviews = query.getResultList();

		session.close();

		return reviews;
	}

	public List<Review> findByProduct(Product product) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		String hql = """
				FROM Review r
				WHERE r.productVariant.product = :product
				ORDER BY r.createdAt DESC
				""";

		Query<Review> query = session.createQuery(hql, Review.class);

		query.setParameter("product", product);

		List<Review> reviews = query.getResultList();

		session.close();

		return reviews;
	}
}