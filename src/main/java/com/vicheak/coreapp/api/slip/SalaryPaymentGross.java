package com.vicheak.coreapp.api.slip;

import com.vicheak.coreapp.api.salarygross.SalaryGross;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "salary_payment_gross")
public class SalaryPaymentGross {

    @EmbeddedId
    private SalaryPaymentGrossKey id;

    @ManyToOne
    @MapsId("salaryPaymentId")
    @JoinColumn(name = "salary_payment_id")
    private SalaryPayment salaryPayment;

    @ManyToOne
    @MapsId("salaryGrossId")
    @JoinColumn(name = "salary_gross_id")
    private SalaryGross salaryGross;

    @Column(name = "salary_payment_gross_amount")
    private BigDecimal amount;

}
