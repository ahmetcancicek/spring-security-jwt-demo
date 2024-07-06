package com.auth.demo.repository;

import com.auth.demo.model.PasswordResetToken;
import com.auth.demo.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);

    @Modifying
    @Query("UPDATE PasswordResetToken prt SET prt.active = :active WHERE prt.user = :user")
    int updatePasswordResetTokenStatusByUser(@Param("active") Boolean active, @Param("user") User user);

}
