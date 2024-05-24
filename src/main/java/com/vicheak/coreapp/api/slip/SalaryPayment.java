package com.vicheak.coreapp.api.slip;

import com.vicheak.coreapp.api.employee.Employee;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "salary_payments")
public class SalaryPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "salary_payment_id")
    private Long id;

    @Column(name = "salary_payment_uuid", unique = true, nullable = false)
    private String uuid;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "salary_payment_datetime", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "salary_payment_basesalary", nullable = false)
    private BigDecimal baseSalary;

    @Column(name = "salary_payment_month", nullable = false)
    private Integer month;

    @Column(name = "salary_payment_year", nullable = false)
    private Integer year;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "salary_payment_paydatetime")
    private LocalDateTime paymentDateTime;

    @ManyToOne
    @JoinColumn(name = "emp_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "payment_state_id")
    private PaymentState paymentState;

}
