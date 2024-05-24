package com.vicheak.coreapp.repository;

import com.vicheak.coreapp.api.salarygross.SalaryGross;
import com.vicheak.coreapp.api.salarygross.SalaryGrossRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class SalaryGrossRepositoryTests {

    @Autowired
    private SalaryGrossRepository salaryGrossRepository;

    @Test
    public void testFindByNameIgnoreCase(){
        //given

        //when
        Optional<SalaryGross> salaryGrossOptional = salaryGrossRepository.findByNameIgnoreCase("allowance");
        SalaryGross salaryGross = salaryGrossOptional.orElse(null);

        //then
        Assertions.assertNotNull(salaryGross);
    }

    @Test
    public void testFindByNameIgnoreCaseThrow(){
        //given

        //when
        Optional<SalaryGross> salaryGrossOptional = salaryGrossRepository.findByNameIgnoreCase("allowances");
        SalaryGross salaryGross = salaryGrossOptional.orElse(null);

        //then
        Assertions.assertNull(salaryGross);
    }

    @Test
    public void testExistsByNameIgnoreCase(){
        //given

        //when
        Boolean isExisted = salaryGrossRepository.existsByNameIgnoreCase("allow");

        //then
        Assertions.assertFalse(isExisted);
    }

    @Test
    public void testFindByGrossTypeId(){
        //given

        //when
        List<SalaryGross> salaryGrossList = salaryGrossRepository.findByGrossTypeId(1);

        //then
        Assertions.assertNotEquals(salaryGrossList, Collections.emptyList());
        Assertions.assertEquals(salaryGrossList.size(), 3);
        Assertions.assertEquals(salaryGrossList.get(0).getName(), "Allowance");
    }

    @Test
    public void testFindByGrossTypeIdNegative(){
        //given

        //when
        List<SalaryGross> salaryGrossList = salaryGrossRepository.findByGrossTypeId(-1);

        //then
        Assertions.assertEquals(salaryGrossList, Collections.emptyList());
    }

}
