package com.vicheak.coreapp.api.employee;

import com.vicheak.coreapp.api.department.Department;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "employees", uniqueConstraints =
@UniqueConstraint(columnNames = {"emp_fname", "emp_lname", "emp_birthdate"}))
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id")
    private Long id;

    @Column(name = "emp_uuid", unique = true, nullable = false)
    private String uuid;

    @Column(name = "emp_fname", length = 50, nullable = false)
    private String firstName;

    @Column(name = "emp_lname", length = 50, nullable = false)
    private String lastName;

    @Column(name = "emp_gender", length = 10, nullable = false)
    private String gender;

    @Temporal(TemporalType.DATE)
    @Column(name = "emp_birthdate", nullable = false)
    private LocalDate birthDate;

    @Column(name = "emp_address", nullable = false)
    private String address;

    @Column(name = "emp_email", length = 100, unique = true, nullable = false)
    private String email;

    @Column(name = "emp_phone", length = 50, unique = true, nullable = false)
    private String phone;

    @Column(name = "emp_basesalary")
    private BigDecimal baseSalary;

    @Column(name = "emp_active")
    private Boolean active;

    @Column(name = "emp_image")
    private String imageName;

    @ManyToOne
    @JoinColumn(name = "dept_id")
    private Department department;

}
