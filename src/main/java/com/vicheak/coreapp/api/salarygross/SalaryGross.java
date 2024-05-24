package com.vicheak.coreapp.api.salarygross;

import com.vicheak.coreapp.api.slip.SalaryPayment;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
