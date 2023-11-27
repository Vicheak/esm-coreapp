package com.vicheak.coreapp.spec;

import com.vicheak.coreapp.api.salarygross.SalaryGross;
import com.vicheak.coreapp.api.salarygross.SalaryGross_;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
@RequiredArgsConstructor
public class SalaryGrossSpec implements Specification<SalaryGross> {

    private final SalaryGrossFilter salaryGrossFilter;

    @Override
    public Predicate toPredicate(@NonNull Root<SalaryGross> salaryGrossRoot,
                                 @NonNull CriteriaQuery<?> query,
                                 @NonNull CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(salaryGrossFilter.name())) {
            Predicate namePredicate = cb.like(cb.lower(salaryGrossRoot.get(SalaryGross_.NAME)),
                    salaryGrossFilter.name().toLowerCase() + "%");
            predicates.add(namePredicate);
        }

        if (Objects.nonNull(salaryGrossFilter.amount())) {
            //compare the amount of request parameter
            Predicate amountPredicate = cb.equal(salaryGrossRoot.get(SalaryGross_.AMOUNT),
                    salaryGrossFilter.amount());
            predicates.add(amountPredicate);
        }

        //convert from list of Predicate to array of Predicate
        return cb.and(predicates.toArray(Predicate[]::new));
    }

}
