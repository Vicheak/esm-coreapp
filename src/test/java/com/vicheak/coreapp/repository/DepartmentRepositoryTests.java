package com.vicheak.coreapp.repository;

import com.vicheak.coreapp.api.department.Department;
import com.vicheak.coreapp.api.department.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class DepartmentRepositoryTests {

    @Autowired
    private DepartmentRepository departmentRepository;

    //@BeforeEach
    public void setup() {
        Department department = Department.builder()
                .name("IT")
                .description("Information Technology")
                .phone("12345610453")
                .build();

        departmentRepository.save(department);
    }

    @Test
    public void testFindByNameIgnoreCase(){
        //given

        //when
        Optional<Department> departmentOptional = departmentRepository.findByNameIgnoreCase("IT");
        Department department = departmentOptional.orElse(null);

        //then
        assertNotNull(department);
    }

    @Test
    public void testFindByNameIgnoreCaseThrow(){
        //given

        //when
        Optional<Department> departmentOptional = departmentRepository.findByNameIgnoreCase("Its");
        Department department = departmentOptional.orElse(null);

        //then
        assertNull(department);
    }

    @Test
    public void testExistsByNameIgnoreCaseOrPhone(){
        //given

        //when
        Boolean isExisted = departmentRepository.existsByNameIgnoreCaseOrPhone("it", "01234");

        //then
        assertTrue(isExisted);
    }

    @Test
    public void testExistsByNameIgnoreCase(){
        //given

        //when
        Boolean isExisted = departmentRepository.existsByNameIgnoreCase("its");

        //then
        assertFalse(isExisted);
    }

    @Test
    public void testExistsByPhone(){
        //given

        //when
        Boolean isExisted = departmentRepository.existsByPhone("12345610453");

        //then
        assertTrue(isExisted);
    }

}
