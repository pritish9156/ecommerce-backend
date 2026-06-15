package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import entity.Product;
import entity.ProductImage;
import util.HibernateUtil;

public class ProductImageDAO {

	public boolean save(ProductImage image) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();

		try {

			session.persist(image);

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

	public boolean update(ProductImage image) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();

		try {

			session.merge(image);

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

	public boolean delete(ProductImage image) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();

		try {

			ProductImage managedImage = session.merge(image);

			session.remove(managedImage);

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

	public ProductImage findById(Long id) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		ProductImage image = session.find(ProductImage.class, id);

		session.close();

		return image;
	}

	public List<ProductImage> findByProduct(Product product) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		String hql = "FROM ProductImage WHERE product = :product ORDER BY displayOrder";

		Query<ProductImage> query = session.createQuery(hql, ProductImage.class);

		query.setParameter("product", product);

		List<ProductImage> images = query.getResultList();

		session.close();

		return images;
	}
}