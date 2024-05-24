package com.vicheak.coreapp.repository;

import com.vicheak.coreapp.api.salarygross.GrossType;
import com.vicheak.coreapp.api.salarygross.GrossTypeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class GrossTypeRepositoryTests {

    @Autowired
    private GrossTypeRepository grossTypeRepository;

    @Test
    public void testFindByNameIgnoreCase(){
        //given

        //when
        Optional<GrossType> grossTypeOptional = grossTypeRepository.findByNameIgnoreCase("benefit");
        GrossType grossType = grossTypeOptional.orElse(null);

        //then
        assertNotNull(grossType);
    }

    @Test
    public void testFindByNameIgnoreCaseThrow(){
        //given

        //when
        Optional<GrossType> grossTypeOptional = grossTypeRepository.findByNameIgnoreCase("benefits");
        GrossType grossType = grossTypeOptional.orElse(null);

        //then
        assertNull(grossType);
    }

    @Test
    public void testExistsByNameIgnoreCase(){
        //given

        //when
        Boolean isExisted = grossTypeRepository.existsByNameIgnoreCase("benefit");

        //then
        assertTrue(isExisted);
    }

}
