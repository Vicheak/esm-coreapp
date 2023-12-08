package com.vicheak.coreapp.repository;

import com.vicheak.coreapp.api.employee.Employee;
import com.vicheak.coreapp.api.employee.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void testExistsByEmailIgnoreCase(){
        //given

        //when
        Boolean isExisted = employeeRepository.existsByEmailIgnoreCase("Torrance.Connelly@hotmail.com");

        //then
        assertTrue(isExisted);
    }

    @Test
    public void testExistsByPhone(){
        //given

        //when
        Boolean isExisted = employeeRepository.existsByPhone("605-773-4919");

        //then
        assertTrue(isExisted);
    }

    @Test
    public void testFindByUuid(){
        //given

        //when
        Optional<Employee> employeeOptional = employeeRepository.findByUuid("c2087ded-18d6-4a2c-ad60-dbfbbb780c74");
        Employee employee = employeeOptional.orElse(null);

        //then
        assertNotNull(employee);
    }

    @Test
    public void testFindByUuidThrow(){
        //given

        //when
        Optional<Employee> employeeOptional = employeeRepository.findByUuid("c2087ded-18d6-4a2c-ad60-dbfbbb780cF4");
        Employee employee = employeeOptional.orElse(null);

        //then
        assertNull(employee);
    }

    @Test
    public void testFindByDepartmentName(){
        //given

        //when
        List<Employee> employees = employeeRepository.findByDepartmentName("IT");

        //then
        assertNotEquals(employees, Collections.emptyList());
        assertEquals(employees.size(), 2);
        assertEquals(employees.get(0).getUuid(), "c2087ded-18d6-4a2c-ad60-dbfbbb780c74");
    }

    @Test
    public void testFindByDepartmentNameNegative(){
        //given

        //when
        List<Employee> employees = employeeRepository.findByDepartmentName("ITs");

        //then
        assertEquals(employees, Collections.emptyList());
    }

}
