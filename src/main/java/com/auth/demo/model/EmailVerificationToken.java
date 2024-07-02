package com.auth.demo.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "email_verification_token")
public class EmailVerificationToken extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_verification_token_seq")
    @SequenceGenerator(name = "email_verification_token_seq", allocationSize = 1)
    private Long id;

    @Column(name = "token", length = 255, nullable = false, unique = true)
    private String token;

    @OneToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "token_status")
    private TokenStatus tokenStatus;

    public EmailVerificationToken() {

    }

    public EmailVerificationToken(Long id, String token, User user, Instant expiryDate, TokenStatus tokenStatus) {
        this.id = id;
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
        this.tokenStatus = tokenStatus;
    }

    public void setConfirmedStatus() {
        setTokenStatus(TokenStatus.STATUS_CONFIRMED);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public TokenStatus getTokenStatus() {
        return tokenStatus;
    }

    public void setTokenStatus(TokenStatus tokenStatus) {
        this.tokenStatus = tokenStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null | o.getClass() != this.getClass()) return false;

        EmailVerificationToken emailVerificationToken = (EmailVerificationToken) o;

        if (!Objects.equals(emailVerificationToken.token, this.token)) return false;
        if (!Objects.equals(emailVerificationToken.user, this.user)) return false;
        if (!Objects.equals(emailVerificationToken.tokenStatus, this.tokenStatus)) return false;
        return id != null && Objects.equals(emailVerificationToken.id, this.id);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (token != null ? token.hashCode() : 0);
        result = 31 * result + (tokenStatus != null ? tokenStatus.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EmailVerificationToken{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", expiryDate=" + expiryDate +
                ", tokenStatus=" + tokenStatus +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
