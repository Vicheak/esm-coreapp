package com.vicheak.coreapp.spec;

import com.vicheak.coreapp.api.employee.Employee;
import com.vicheak.coreapp.api.employee.Employee_;
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
public class EmployeeSpec implements Specification<Employee> {

    private final EmployeeFilter employeeFilter;

    @Override
    public Predicate toPredicate(@NonNull Root<Employee> employeeRoot,
                                 @NonNull CriteriaQuery<?> query,
                                 @NonNull CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(employeeFilter.firstName())) {
            Predicate firstNamePredicate = cb.like(cb.lower(employeeRoot.get(Employee_.FIRST_NAME)),
                    employeeFilter.firstName().toLowerCase() + '%');
            predicates.add(firstNamePredicate);
        }

        if (Objects.nonNull(employeeFilter.lastName())) {
            Predicate lastNamePredicate = cb.like(cb.lower(employeeRoot.get(Employee_.LAST_NAME)),
                    employeeFilter.lastName().toLowerCase() + '%');
            predicates.add(lastNamePredicate);
        }

        if (Objects.nonNull(employeeFilter.address())) {
            Predicate addressPredicate = cb.like(cb.lower(employeeRoot.get(Employee_.ADDRESS)),
                    '%' + employeeFilter.address().toLowerCase() + '%');
            predicates.add(addressPredicate);
        }

        if (Objects.nonNull(employeeFilter.email())) {
            Predicate emailPredicate = cb.like(cb.lower(employeeRoot.get(Employee_.EMAIL)),
                    employeeFilter.email().toLowerCase() + '%');
            predicates.add(emailPredicate);
        }

        if (Objects.nonNull(employeeFilter.phone())) {
            Predicate phonePredicate = cb.like(cb.lower(employeeRoot.get(Employee_.PHONE)),
                    '%' + employeeFilter.phone() + '%');
            predicates.add(phonePredicate);
        }

        //convert from list of Predicate to array of Predicate
        return cb.and(predicates.toArray(Predicate[]::new));
    }
}
