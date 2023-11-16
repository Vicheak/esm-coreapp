package com.vicheak.coreapp.api.employee;

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
@Table(name = "base_salary_logs")
public class BaseSalaryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "base_salary_log_id")
    private Long id;

    @Column(name = "base_salary_log_uuid", unique = true, nullable = false)
    private String uuid;

    @Column(name = "base_salary_log_description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "base_salary_log_amount", nullable = false)
    private BigDecimal amount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "base_salary_log_datetime", nullable = false)
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "emp_id")
    private Employee employee;

}
