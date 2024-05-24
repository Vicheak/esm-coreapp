package com.vicheak.coreapp.spec;

import com.vicheak.coreapp.api.employee.BaseSalaryLog;
import com.vicheak.coreapp.api.employee.BaseSalaryLog_;
import com.vicheak.coreapp.api.employee.Employee;
import com.vicheak.coreapp.api.employee.Employee_;
import jakarta.persistence.criteria.*;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
@RequiredArgsConstructor
public class BaseSalaryLogSpec implements Specification<BaseSalaryLog> {

    private final BaseSalaryLogFilter baseSalaryLogFilter;

    @Override
    public Predicate toPredicate(@NonNull Root<BaseSalaryLog> baseSalaryLogRoot,
                                 @NonNull CriteriaQuery<?> query,
                                 @NonNull CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<>();

        //access employee entity in relation to baseSalaryLog entity
        Join<BaseSalaryLog, Employee> employeeJoin = baseSalaryLogRoot.join(BaseSalaryLog_.EMPLOYEE);

        if (Objects.nonNull(baseSalaryLogFilter.firstName()))
            predicates.add(
                    filterPredicate(cb, employeeJoin.get(Employee_.FIRST_NAME), baseSalaryLogFilter.firstName()));

        if (Objects.nonNull(baseSalaryLogFilter.lastName()))
            predicates.add(
                    filterPredicate(cb, employeeJoin.get(Employee_.LAST_NAME), baseSalaryLogFilter.lastName()));

        if (Objects.nonNull(baseSalaryLogFilter.startDate())
                && Objects.nonNull(baseSalaryLogFilter.endDate())) {
            Predicate dateTimePredicate = cb.between(baseSalaryLogRoot.get(BaseSalaryLog_.DATE_TIME),
                    baseSalaryLogFilter.startDate(),
                    baseSalaryLogFilter.endDate());
            predicates.add(dateTimePredicate);
        }

        //convert from list of Predicate to array of Predicate
        return cb.and(predicates.toArray(Predicate[]::new));
    }

    //extract sub method from method overriding
    private Predicate filterPredicate(CriteriaBuilder cb, Path<String> path, String predicateItem) {
        return cb.like(cb.lower(path), predicateItem.toLowerCase() + '%');
    }

}
