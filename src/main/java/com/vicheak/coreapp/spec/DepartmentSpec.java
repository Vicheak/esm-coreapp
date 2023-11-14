package com.vicheak.coreapp.spec;

import com.vicheak.coreapp.api.department.Department;
import com.vicheak.coreapp.api.department.Department_;
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
public class DepartmentSpec implements Specification<Department> {

    private final DepartmentFilter departmentFilter;

    @Override
    public Predicate toPredicate(@NonNull Root<Department> departmentRoot,
                                 @NonNull CriteriaQuery<?> query,
                                 @NonNull CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(departmentFilter.name())) {
            Predicate namePredicate = cb.like(cb.lower(departmentRoot.get(Department_.NAME)),
                    '%' + departmentFilter.name().toLowerCase() + '%');
            predicates.add(namePredicate);
        }

        if (Objects.nonNull(departmentFilter.phone())) {
            Predicate phonePredicate = cb.like(departmentRoot.get(Department_.PHONE),
                    '%' + departmentFilter.phone().toLowerCase() + '%');
            predicates.add(phonePredicate);
        }

        //convert from list of Predicate to array of Predicate
        return cb.and(predicates.toArray(Predicate[]::new));
    }

}
