package util;

import dao.UserDAO;
import entity.User;
import entity.enums.Role;

public class AdminInitializer {

	private final UserDAO userDAO;

	public AdminInitializer() {
		userDAO = new UserDAO();
	}

	public void initializeAdmin() {

		try {

			User existingAdmin = userDAO.findAdmin();

			if (existingAdmin != null) {

				if (!existingAdmin.isEmailVerified()) {
					existingAdmin.setEmailVerified(true);
				}

				if (!existingAdmin.isActive()) {
					existingAdmin.setActive(true);
				}

				userDAO.update(existingAdmin);

				System.out.println("Admin account already exists.");

				return;
			}

			User admin = new User();

			admin.setFirstName("Admin");

			admin.setLastName("ShopVerse");

			admin.setEmail("shopverse.official2026@gmail.com");

			admin.setMobileNumber("9999999999");

			admin.setPassword(BCryptUtil.hashPassword("ShopVerse@123"));

			admin.setRole(Role.ADMIN);

			admin.setEmailVerified(true);

			admin.setActive(true);

			boolean saved = userDAO.save(admin);

			if (saved) {

				System.out.println("Default admin account created successfully.");

			} else {

				System.err.println("Failed to create default admin account.");
			}

		} catch (Exception e) {

			System.err.println("Error while initializing admin account.");

			e.printStackTrace();
		}
	}
}