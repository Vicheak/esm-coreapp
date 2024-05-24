package com.vicheak.coreapp.repository;

import com.vicheak.coreapp.api.employee.Employee;
import com.vicheak.coreapp.api.slip.PaymentState;
import com.vicheak.coreapp.api.slip.SalaryPayment;
import com.vicheak.coreapp.api.slip.SalaryPaymentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class SalaryPaymentRepositoryTests {

    @Autowired
    private SalaryPaymentRepository salaryPaymentRepository;

    @BeforeEach
    public void setup() {
        SalaryPayment salaryPayment = SalaryPayment.builder()
                .uuid("778fab01-cade-4508-ba32-2f757a448722")
                .dateTime(LocalDateTime.now())
                .baseSalary(BigDecimal.valueOf(1500.00))
                .month(11)
                .year(2023)
                .employee(Employee.builder().id(1L).build())
                .paymentState(PaymentState.builder().id(1).build())
                .build();

        salaryPaymentRepository.save(salaryPayment);
    }

    @Test
    public void testFindByUuid(){
        //given

        //when
        Optional<SalaryPayment> salaryPaymentOptional =
                salaryPaymentRepository.findByUuid("778fab01-cade-4508-ba32-2f757a448722");
        SalaryPayment salaryPayment = salaryPaymentOptional.orElse(null);

        //then
        Assertions.assertNotNull(salaryPayment);
    }

    @Test
    public void testFindByUuidThrow(){
        //given

        //when
        Optional<SalaryPayment> salaryPaymentOptional =
                salaryPaymentRepository.findByUuid("778fab01-cade-4508-ba32-2f757a44872g");
        SalaryPayment salaryPayment = salaryPaymentOptional.orElse(null);

        //then
        Assertions.assertNull(salaryPayment);
    }

    @Test
    public void testFindByEmployeeId(){
        //given

        //when
        List<SalaryPayment> salaryPayments = salaryPaymentRepository.findByEmployeeId(1L);

        //then
        Assertions.assertNotEquals(salaryPayments, Collections.emptyList());
        Assertions.assertEquals(salaryPayments.size(), 1);
        Assertions.assertEquals(salaryPayments.get(0).getUuid(), "778fab01-cade-4508-ba32-2f757a448722");
    }

    @Test
    public void testFindByEmployeeIdNegative(){
        //given

        //when
        List<SalaryPayment> salaryPayments = salaryPaymentRepository.findByEmployeeId(-1L);

        //then
        Assertions.assertEquals(salaryPayments, Collections.emptyList());
    }

}
