package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import entity.Address;
import entity.User;
import util.HibernateUtil;

public class AddressDAO {

	public boolean save(Address address) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();

		try {

			session.persist(address);

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

	public boolean update(Address address) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();

		try {

			session.merge(address);

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

	public boolean delete(Long addressId) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();

		try {

			Address address = session.find(Address.class, addressId);

			if (address == null) {
				tx.rollback();
				return false;
			}

			session.remove(address);

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

	public Address findById(Long id) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Address address = session.find(Address.class, id);

		session.close();

		return address;
	}

	public List<Address> findByUser(User user) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		String hql = "FROM Address WHERE user = :user";

		Query<Address> query = session.createQuery(hql, Address.class);

		query.setParameter("user", user);

		List<Address> addresses = query.getResultList();

		session.close();

		return addresses;
	}

	public boolean existsAddress(Address address) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		String hql = "From Address WHERE addressLine1 = :addressLine1 and postalCode = :postalCode and user = :user";
		
		Query<Address> query = session.createQuery(hql, Address.class);
		
		query.setParameter("addressLine1", address.getAddressLine1());
		query.setParameter("postalCode", address.getPostalCode());
		query.setParameter("user", address.getUser());
		
		Address result = query.uniqueResult();
		
		if(result != null)
			return true;
		
		return false;
	}
}