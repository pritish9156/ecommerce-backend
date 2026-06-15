package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import entity.Product;
import entity.ProductVariant;
import util.HibernateUtil;

public class ProductVariantDAO {

	public boolean save(ProductVariant productVariant) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();

		try {

			session.persist(productVariant);

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

	public boolean update(ProductVariant productVariant) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();

		try {

			session.merge(productVariant);

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

	public ProductVariant findById(Long id) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		ProductVariant productVariant = session.find(ProductVariant.class, id);

		session.close();

		return productVariant;
	}

	public ProductVariant findBySku(String sku) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		String hql = "FROM ProductVariant WHERE sku = :sku";

		Query<ProductVariant> query = session.createQuery(hql, ProductVariant.class);

		query.setParameter("sku", sku);

		ProductVariant productVariant = query.uniqueResult();

		session.close();

		return productVariant;
	}

	public List<ProductVariant> findAll() {

		Session session = HibernateUtil.getSessionFactory().openSession();

		String hql = "FROM ProductVariant WHERE isActive = true";

		Query<ProductVariant> query = session.createQuery(hql, ProductVariant.class);

		List<ProductVariant> variants = query.getResultList();

		session.close();

		return variants;
	}

	public List<ProductVariant> findByProduct(Product product) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		String hql = "FROM ProductVariant WHERE product = :product AND isActive = true";

		Query<ProductVariant> query = session.createQuery(hql, ProductVariant.class);

		query.setParameter("product", product);

		List<ProductVariant> variants = query.getResultList();

		session.close();

		return variants;
	}
}