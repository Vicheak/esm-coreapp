package com.vicheak.coreapp.api.auth;

import com.vicheak.coreapp.api.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<User, Long> {

    @Modifying
    @Query("UPDATE User AS u SET u.verifiedCode = :verifiedCode WHERE u.username = :username")
    void updateVerifiedCode(String username, String verifiedCode);

    Optional<User> findByEmailAndVerifiedCodeAndIsDeletedFalse(String email, String verifiedCode);

    Optional<User> findByEmailAndPasswordAndIsVerifiedTrueAndIsDeletedFalse(String email, String password);

    Boolean existsByEmailAndIsVerifiedTrueAndIsDeletedFalse(String email);

    @Modifying
    @Query("UPDATE User AS u SET u.verifiedCode = :verifiedCode WHERE u.email = :email")
    void updateVerifiedCodeByEmail(String email, String verifiedCode);

    Boolean existsByEmailAndVerifiedCodeNotNullAndIsVerifiedTrue(String email);

}
