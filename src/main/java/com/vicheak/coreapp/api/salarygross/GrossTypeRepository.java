package com.vicheak.coreapp.api.salarygross;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GrossTypeRepository extends JpaRepository<GrossType, Integer> {

    Optional<GrossType> findByNameIgnoreCase(String name);

    Boolean existsByNameIgnoreCase(String name);

}
