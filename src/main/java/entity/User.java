package entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import entity.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;
	
	@Column(nullable = false, length = 50)
	private String firstName;
	
	@Column(nullable = false, length = 50)
	private String lastName;
	
	@Column(unique = true, nullable = false, length = 100)
	private String email;
	
	@Column(nullable = false, length = 100)
	@JsonIgnore
	private String password;
	
	@Column(unique = true, nullable = false, length = 15)
	private String mobileNumber;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;
	
	private boolean isActive;
	
	private boolean emailVerified;
	
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	@Column(nullable = false)
	private LocalDateTime updatedAt;
	
	@PrePersist
	public void onCreate() {
		
		if (role == null) {
	        role = Role.CUSTOMER;
	    }
		
		isActive = true;
		emailVerified = false;
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}
	
	@PreUpdate
	public void onUpdate() {
		updatedAt = LocalDateTime.now();
	}

	public User(Long id, String firstName, String lastName, String email, String password, String mobileNumber,
			Role role, boolean isActive, boolean emailVerified, LocalDateTime createdAt, LocalDateTime updatedAt) {

		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.mobileNumber = mobileNumber;
		this.role = role;
		this.isActive = isActive;
		this.emailVerified = emailVerified;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	
	public User() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", password=" + password + ", mobileNumber=" + mobileNumber + ", role=" + role + ", isActive="
				+ isActive + ", emailVerified=" + emailVerified + ", createdAt=" + createdAt + ", updatedAt="
				+ updatedAt + "]";
	}
	
}

