package com.vicheak.coreapp.api.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUuid(String uuid);

    Boolean existsByUsernameIgnoreCase(String username);

    Boolean existsByEmailIgnoreCase(String email);

    @Query("SELECT EXISTS (SELECT u FROM User AS u WHERE u.uuid = :uuid)")
    Boolean checkUserByUuid(String uuid);

    @Modifying
    @Query("UPDATE User AS u SET u.isDeleted = :isDeleted WHERE u.uuid = :uuid")
    void updateIsDeletedStatusByUuid(String uuid, Boolean isDeleted);

}
