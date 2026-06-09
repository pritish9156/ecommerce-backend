package auth;

import java.time.LocalDateTime;

import entity.User;
import entity.enums.TokenType;
import jakarta.persistence.*;

@Entity
@Table(name = "verification_tokens")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @Column(nullable = false, unique = true, length = 100)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenType tokenType;

    @Column(nullable = false)
    private LocalDateTime expiryTime;

    @Column(nullable = false)
    private boolean isUsed;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime usedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        isUsed = false;
    }

	public VerificationToken(User user, String token, TokenType tokenType, LocalDateTime expiryTime, boolean isUsed,
			LocalDateTime createdAt, LocalDateTime usedAt) {
		super();
		this.user = user;
		this.token = token;
		this.tokenType = tokenType;
		this.expiryTime = expiryTime;
		this.isUsed = isUsed;
		this.createdAt = createdAt;
		this.usedAt = usedAt;
	}
    
    public VerificationToken() {
		// TODO Auto-generated constructor stub
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public TokenType getTokenType() {
		return tokenType;
	}

	public void setTokenType(TokenType tokenType) {
		this.tokenType = tokenType;
	}

	public LocalDateTime getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(LocalDateTime expiryTime) {
		this.expiryTime = expiryTime;
	}

	public boolean isUsed() {
		return isUsed;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUsedAt() {
		return usedAt;
	}

	public void setUsedAt(LocalDateTime usedAt) {
		this.usedAt = usedAt;
	}

	@Override
	public String toString() {
		return "VerificationToken [id=" + id + ", user=" + user + ", token=" + token + ", tokenType=" + tokenType
				+ ", expiryTime=" + expiryTime + ", isUsed=" + isUsed + ", createdAt=" + createdAt + ", usedAt="
				+ usedAt + "]";
	}
    
}