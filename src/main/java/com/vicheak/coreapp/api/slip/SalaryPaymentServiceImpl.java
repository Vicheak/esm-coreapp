package com.vicheak.coreapp.api.slip;

import com.vicheak.coreapp.api.employee.Employee;
import com.vicheak.coreapp.api.employee.EmployeeRepository;
import com.vicheak.coreapp.api.salarygross.SalaryGross;
import com.vicheak.coreapp.api.salarygross.SalaryGrossRepository;
import com.vicheak.coreapp.api.slip.web.TransactionSalaryPaymentDto;
import com.vicheak.coreapp.api.slip.web.UpdatePaymentStateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SalaryPaymentServiceImpl implements SalaryPaymentService {

    private final SalaryPaymentRepository salaryPaymentRepository;
    private final SalaryPaymentMapper salaryPaymentMapper;
    private final SalaryPaymentGrossRepository salaryPaymentGrossRepository;
    private final SalaryGrossRepository salaryGrossRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    @Override
    public void createNewSalaryPayment(TransactionSalaryPaymentDto transactionSalaryPaymentDto) {
        //load specific employee by employee id
        Employee selectedEmployee = employeeRepository.findByUuid(transactionSalaryPaymentDto.employeeUuid())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Employee with uuid = %s has not been found...!"
                                        .formatted(transactionSalaryPaymentDto.employeeUuid()))
                );

        //check if the selected employee's base salary is defined
        if (Objects.isNull(selectedEmployee.getBaseSalary()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Please define base salary for employee with uuid = %s before generating slip!"
                            .formatted(transactionSalaryPaymentDto.employeeUuid()));

        //check salary gross id list for salary payment gross bridge entity
        if (Objects.nonNull(transactionSalaryPaymentDto.salaryGrossIds())) {
            //check if salary gross id list exists in the database
            boolean isNotFound = transactionSalaryPaymentDto.salaryGrossIds().stream()
                    .anyMatch(salaryGrossId -> !salaryGrossRepository.existsById(salaryGrossId));

            if (isNotFound)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Salary Gross Id does not exist...!");
        }

        //map salary payment object
        SalaryPayment salaryPayment =
                salaryPaymentMapper.fromTransactionSalaryPaymentDto(transactionSalaryPaymentDto);

        salaryPayment.setUuid(UUID.randomUUID().toString());
        salaryPayment.setDateTime(LocalDateTime.now());
        salaryPayment.setBaseSalary(selectedEmployee.getBaseSalary());
        salaryPayment.setEmployee(selectedEmployee);
        salaryPayment.setPaymentState(PaymentState.builder().id(1).build()); //set unpaid for default

        //save salary payment
        salaryPaymentRepository.save(salaryPayment);

        if (Objects.nonNull(transactionSalaryPaymentDto.salaryGrossIds())) {
            transactionSalaryPaymentDto.salaryGrossIds().forEach(salaryGrossId -> {
                SalaryPaymentGross salaryPaymentGross = new SalaryPaymentGross();

                //load salary gross resource by salary gross id
                SalaryGross salaryGross = salaryGrossRepository.findById(salaryGrossId)
                        .orElseThrow(
                                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                        "Salary Gross with id = %d has not been found...!"
                                                .formatted(salaryGrossId))
                        );

                //set the composite primary key
                salaryPaymentGross.setId(SalaryPaymentGrossKey.builder()
                        .salaryPaymentId(salaryPayment.getId())
                        .salaryGrossId(salaryGrossId)
                        .build());

                salaryPaymentGross.setSalaryPayment(salaryPayment);
                salaryPaymentGross.setSalaryGross(salaryGross);

                //set amount value from loaded salary gross resource
                salaryPaymentGross.setAmount(salaryGross.getAmount());

                //save salary payment gross
                salaryPaymentGrossRepository.save(salaryPaymentGross);
            });
        }
    }

    @Transactional
    @Override
    public void updatePaymentStateByUuid(String uuid, UpdatePaymentStateDto updatePaymentStateDto) {
        //load salary payment by uuid
        SalaryPayment salaryPayment = salaryPaymentRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Salary payment with uuid = %s has not been found...!"
                                        .formatted(uuid))
                );

        salaryPayment = salaryPaymentMapper.mapFromUpdatePaymentStateDto(salaryPayment, updatePaymentStateDto);

        //update salary payment into the database
        salaryPaymentRepository.save(salaryPayment);
    }

    @Transactional
    @Override
    public void deleteSalaryPaymentByUuid(String uuid) {
        //load salary payment by uuid
        SalaryPayment salaryPayment = salaryPaymentRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Salary payment with uuid = %s has not been found...!"
                                        .formatted(uuid))
                );

        //check the salary payment gross and delete all the related ones
        salaryPaymentGrossRepository.deleteBySalaryPayment(salaryPayment);

        //delete salary payment from the database
        salaryPaymentRepository.delete(salaryPayment);
    }

}
