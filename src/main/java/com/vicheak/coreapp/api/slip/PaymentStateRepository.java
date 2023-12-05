package com.vicheak.coreapp.api.slip;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentStateRepository extends JpaRepository<PaymentState, Integer> {

    Optional<PaymentState> findByStatusIgnoreCase(String status);

    Boolean existsByStatusIgnoreCase(String status);

}
