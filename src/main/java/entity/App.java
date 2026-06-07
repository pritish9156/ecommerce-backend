package entity;

import org.hibernate.Session;
import org.hibernate.Transaction;

import util.HibernateUtil;

public class App{
	
	public static void main(String[] args) {
		Session session = HibernateUtil.getSessionFactory().openSession();
	
	
	}
}