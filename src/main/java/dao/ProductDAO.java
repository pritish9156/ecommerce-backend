package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import entity.Product;
import util.HibernateUtil;

public class ProductDAO {

	public boolean save(Product product) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();

		try {

			session.persist(product);

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

	public boolean update(Product product) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();

		try {

			session.merge(product);

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

	public Product findById(Long id) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		String hql = "SELECT DISTINCT p FROM Product p " + "LEFT JOIN FETCH p.tags " + "WHERE p.id = :id";

		Query<Product> query = session.createQuery(hql, Product.class);

		query.setParameter("id", id);

		Product product = query.uniqueResult();

		session.close();

		return product;
	}

	public Product findByName(String name) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		String hql = "FROM Product WHERE name = :name";

		Query<Product> query = session.createQuery(hql, Product.class);

		query.setParameter("name", name);

		Product product = query.uniqueResult();

		session.close();

		return product;
	}

	public Product findBySlug(String slug) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		String hql = "FROM Product WHERE slug = :slug";

		Query<Product> query = session.createQuery(hql, Product.class);

		query.setParameter("slug", slug);

		Product product = query.uniqueResult();

		session.close();

		return product;
	}

	public List<Product> findAll() {

		Session session = HibernateUtil.getSessionFactory().openSession();

		String hql = "SELECT DISTINCT p FROM Product p " + "LEFT JOIN FETCH p.tags";

		Query<Product> query = session.createQuery(hql, Product.class);

		List<Product> products = query.getResultList();

		session.close();

		return products;
	}

	public List<Product> searchProducts(String keyword, Long brandId, String sortBy, String sortDirection, int page,
			int size) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		StringBuilder hql = new StringBuilder(
				"FROM Product p JOIN ProductVariant pv ON pv.product = p WHERE p.isActive = true");

		if (keyword != null && !keyword.trim().isEmpty()) {

			hql.append(" AND LOWER(p.name) LIKE :keyword");
		}

		if (brandId != null) {

			hql.append(" AND p.brand.id = :brandId");
		}

		if (sortBy != null && !sortBy.isBlank()) {

			switch (sortBy.toLowerCase()) {

			case "name":

				hql.append(" ORDER BY p.name ASC");

				break;

			case "rating":

				hql.append(" ORDER BY p.averageRating DESC");

				break;

			case "newest":

				hql.append(" ORDER BY p.createdAt DESC");

				break;

			case "price_low":

				hql.append(" ORDER BY pv.price ASC");

				break;

			case "price_high":

				hql.append(" ORDER BY pv.price DESC");

				break;

			default:

				hql.append(" ORDER BY p.createdAt DESC");

			}

		} else {

			hql.append(" ORDER BY p.createdAt DESC");

		}

		Query<Product> query = session.createQuery(hql.toString(), Product.class);

		if (keyword != null && !keyword.trim().isEmpty()) {

			query.setParameter("keyword", "%" + keyword.toLowerCase() + "%");
		}

		if (brandId != null) {

			query.setParameter("brandId", brandId);
		}

		query.setFirstResult(page * size);

		query.setMaxResults(size);

		List<Product> products = query.getResultList();

		session.close();

		System.out.println(hql.toString());

		return products;
	}

	public Long countProducts(String keyword, Long brandId, String sortBy, String sortDirection) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		StringBuilder hql = new StringBuilder("SELECT COUNT(p) FROM Product p WHERE p.isActive = true");

		if (keyword != null && !keyword.trim().isEmpty()) {

			hql.append(" AND LOWER(p.name) LIKE :keyword");
		}

		if (brandId != null) {

			hql.append(" AND p.brand.id = :brandId");
		}

		Query<Long> query = session.createQuery(hql.toString(), Long.class);

		if (keyword != null && !keyword.trim().isEmpty()) {

			query.setParameter("keyword", "%" + keyword.toLowerCase() + "%");
		}

		if (brandId != null) {

			query.setParameter("brandId", brandId);
		}

		Long count = query.uniqueResult();

		session.close();

		return count;
	}

	public List<Product> findRelatedProducts(Long categoryId, Long currentProductId) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		String hql = "From Product p where p.category.id = :categoryId AND p.id <> :currentProductId AND isActive = true";

		Query<Product> query = session.createQuery(hql, Product.class);

		query.setParameter("categoryId", categoryId);
		query.setParameter("currentProductId", currentProductId);

		query.setMaxResults(8);

		List<Product> relatedProductList = query.getResultList();

		session.close();

		return relatedProductList;
	}
}