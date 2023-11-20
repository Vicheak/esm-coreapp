package com.vicheak.coreapp.api.salarygross;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "salary_gross")
public class SalaryGross {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "salary_gross_id")
    private Integer id;

    @Column(name = "salary_gross_name", length = 50, unique = true, nullable = false)
    private String name;

    @Column(name = "salary_gross_amount", nullable = false)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "gross_type_id")
    private GrossType grossType;

}
