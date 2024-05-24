package com.vicheak.coreapp.repository;

import com.vicheak.coreapp.api.employee.BaseSalaryLog;
import com.vicheak.coreapp.api.employee.BaseSalaryLogRepository;
import com.vicheak.coreapp.api.employee.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class BaseSalaryRepositoryTests {

    @Autowired
    private BaseSalaryLogRepository baseSalaryLogRepository;

    @BeforeEach
    public void setup(){
        BaseSalaryLog baseSalaryLog = BaseSalaryLog.builder()
                .uuid("3f1ea8ea-d17d-4acd-a7b5-b1e429280frq")
                .description("Working hard!")
                .amount(BigDecimal.valueOf(1600.00))
                .dateTime(LocalDateTime.now())
                .employee(Employee.builder().id(1L).build())
                .build();

        baseSalaryLogRepository.save(baseSalaryLog);
    }

    @Test
    public void testFindBaseSalaryLogByUuid(){
        //given

        //when
        Optional<BaseSalaryLog> baseSalaryLogOptional =
                baseSalaryLogRepository.findBaseSalaryLogByUuid("3f1ea8ea-d17d-4acd-a7b5-b1e429280frq");
        BaseSalaryLog baseSalaryLog = baseSalaryLogOptional.orElse(null);

        //then
        assertNotNull(baseSalaryLog);
    }

    @Test
    public void testFindBaseSalaryLogByUuidThrow(){
        //given

        //when
        Optional<BaseSalaryLog> baseSalaryLogOptional =
                baseSalaryLogRepository.findBaseSalaryLogByUuid("3f1ea8ea-d17d-4acd-a7b5-b1e429280fro");
        BaseSalaryLog baseSalaryLog = baseSalaryLogOptional.orElse(null);

        //then
        assertNull(baseSalaryLog);
    }

    @Test
    public void testFindByEmployeeId(){
        //given

        //when
        List<BaseSalaryLog> baseSalaryLogs = baseSalaryLogRepository.findByEmployeeId(1L);

        //then
        assertNotEquals(baseSalaryLogs, Collections.emptyList());
        assertEquals(baseSalaryLogs.size(), 1);
        assertEquals(baseSalaryLogs.get(0).getUuid(), "3f1ea8ea-d17d-4acd-a7b5-b1e429280frq");
    }

    @Test
    public void testFindByEmployeeIdNegative(){
        //given

        //when
        List<BaseSalaryLog> baseSalaryLogs = baseSalaryLogRepository.findByEmployeeId(-1L);

        //then
        assertEquals(baseSalaryLogs, Collections.emptyList());
    }

}
