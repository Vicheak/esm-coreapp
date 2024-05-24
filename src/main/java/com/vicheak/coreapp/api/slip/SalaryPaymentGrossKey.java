package com.vicheak.coreapp.api.slip;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Embeddable
public class SalaryPaymentGrossKey implements Serializable {

    @Column(name = "salary_payment_id")
    private Long salaryPaymentId;

    @Column(name = "salary_gross_id")
    private Integer salaryGrossId;

}
