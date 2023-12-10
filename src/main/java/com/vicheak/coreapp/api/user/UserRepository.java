package com.vicheak.coreapp.api.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUsernameIgnoreCase(String username);

    Boolean existsByEmailIgnoreCase(String email);

}
