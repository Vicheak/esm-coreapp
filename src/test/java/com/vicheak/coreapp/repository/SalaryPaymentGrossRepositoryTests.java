package com.vicheak.coreapp.repository;

import com.vicheak.coreapp.api.employee.Employee;
import com.vicheak.coreapp.api.salarygross.SalaryGross;
import com.vicheak.coreapp.api.slip.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@DataJpaTest
public class SalaryPaymentGrossRepositoryTests {

    @Autowired
    private SalaryPaymentRepository salaryPaymentRepository;
    @Autowired
    private SalaryPaymentGrossRepository salaryPaymentGrossRepository;

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

        SalaryPaymentGross salaryPaymentGross = SalaryPaymentGross.builder()
                .id(SalaryPaymentGrossKey.builder()
                        .salaryPaymentId(salaryPayment.getId())
                        .salaryGrossId(1)
                        .build())
                .salaryPayment(salaryPayment)
                .salaryGross(SalaryGross.builder().id(1).build())
                .amount(BigDecimal.valueOf(25.00))
                .build();

        salaryPaymentGrossRepository.save(salaryPaymentGross);
    }

    @Test
    public void testFindBySalaryPaymentId(){
        //given

        //when
        List<SalaryPaymentGross> salaryPaymentGrossList =
                salaryPaymentGrossRepository.findBySalaryPaymentId(1L);

        //then
        Assertions.assertNotEquals(salaryPaymentGrossList, Collections.emptyList());
        Assertions.assertEquals(salaryPaymentGrossList.size(), 1);
        Assertions.assertEquals(salaryPaymentGrossList.get(0).getSalaryPayment().getUuid(), "778fab01-cade-4508-ba32-2f757a448722");
    }

    @Test
    public void testFindBySalaryPaymentIdNegative(){
        //given

        //when
        List<SalaryPaymentGross> salaryPaymentGrossList =
                salaryPaymentGrossRepository.findBySalaryPaymentId(-1L);

        //then
        Assertions.assertEquals(salaryPaymentGrossList, Collections.emptyList());
    }

    @Test
    public void testDeleteBySalaryPayment(){
        //given
        SalaryPayment salaryPayment = SalaryPayment.builder()
                .id(1L)
                .build();

        //when
        salaryPaymentGrossRepository.deleteBySalaryPayment(salaryPayment);
        List<SalaryPaymentGross> salaryPaymentGrossList =
                salaryPaymentGrossRepository.findBySalaryPaymentId(1L);

        //then
        Assertions.assertEquals(salaryPaymentGrossList, Collections.emptyList());
    }

}
