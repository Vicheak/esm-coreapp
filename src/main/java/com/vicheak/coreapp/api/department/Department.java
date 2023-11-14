package com.vicheak.coreapp.api.department;

import com.vicheak.coreapp.api.employee.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_id")
    private Long id;

    @Column(name = "dept_name", length = 50, unique = true, nullable = false)
    private String name;

    @Column(name = "dept_description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "dept_phone", length = 50, unique = true, nullable = false)
    private String phone;

}
