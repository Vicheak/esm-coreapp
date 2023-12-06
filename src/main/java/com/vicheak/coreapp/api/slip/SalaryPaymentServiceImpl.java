package com.vicheak.coreapp.api.slip;

import com.vicheak.coreapp.api.employee.Employee;
import com.vicheak.coreapp.api.employee.EmployeeRepository;
import com.vicheak.coreapp.api.salarygross.SalaryGross;
import com.vicheak.coreapp.api.salarygross.SalaryGrossRepository;
import com.vicheak.coreapp.api.slip.web.TransactionSalaryPaymentDto;
import com.vicheak.coreapp.api.slip.web.UpdatePaymentStateDto;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
        //check a valid transaction
        checkValidTransaction(transactionSalaryPaymentDto);

        //load specific employee by employee id
        Optional<Employee> selectedEmployeeOptional = employeeRepository.findByUuid(transactionSalaryPaymentDto.employeeUuid());
        Employee selectedEmployee;
        if (selectedEmployeeOptional.isPresent())
            selectedEmployee = selectedEmployeeOptional.get();
        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Something went wrong with the database!");

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
    public Map<Integer, String> uploadSalaryPayments(MultipartFile excelFile) {
        //check if the file is empty
        if (excelFile.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Please upload one prepared Excel file!");

        //map to customize exception from excel file
        Map<Integer, String> exceptionMessages = new HashMap<>();

        //create list of dto to store transaction salary payment
        List<TransactionSalaryPaymentDto> transactionData = new ArrayList<>();

        Workbook workbook;

        try {
            //create workbook instance
            workbook = new XSSFWorkbook(excelFile.getInputStream());
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    ex.getMessage());
        }

        //get specific sheet from Excel file
        Sheet sheet = workbook.getSheet("salary_slip_sheet");

        //check if the sheet is not found
        if (Objects.isNull(sheet))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The specified sheet is not correct! Please name the sheet by \"salary_slip_sheet\"");

        //access each row from Excel sheet
        Iterator<Row> rowIterator = sheet.iterator();

        //skip the row header from Excel sheet
        if (rowIterator.hasNext())
            rowIterator.next();

        while (rowIterator.hasNext()) {
            try {
                boolean checkValidation = true;
                int columnIndex = 0;

                Row row = rowIterator.next();

                //access each cell within each row
                Cell rowNumberCell = row.getCell(columnIndex++);
                int rowNumber = (int) rowNumberCell.getNumericCellValue();

                Cell employeeUuidCell = row.getCell(columnIndex++);
                String employeeUuid = employeeUuidCell.getStringCellValue();

                Cell monthCell = row.getCell(columnIndex++);
                int month = (int) monthCell.getNumericCellValue();

                Cell yearCell = row.getCell(columnIndex++);
                int year = (int) yearCell.getNumericCellValue();

                Cell salaryGrossIdsCell = row.getCell(columnIndex);
                String salaryGrossIds = Objects.nonNull(salaryGrossIdsCell) ? salaryGrossIdsCell.getStringCellValue() : "";

                //check if the row is empty
                if (rowNumber == 0 && employeeUuid.isEmpty() && month == 0 && year == 0 && salaryGrossIds.isEmpty())
                    continue;

                //validate row number
                if (rowNumber <= 0) {
                    rowNumber = -1;
                    exceptionMessages.put(rowNumber, "Row number must not be a negative number or 0!");
                    checkValidation = false;
                }

                //validate employee uuid
                if (employeeUuid.isEmpty() || employeeUuid.isBlank()) {
                    exceptionMessages.put(rowNumber, "Employee uuid must not be blank!");
                    checkValidation = false;
                }

                //validate month
                if (month <= 0 || month > 12) {
                    exceptionMessages.put(rowNumber, "Month must be between 1 and 12!");
                    checkValidation = false;
                }

                //validate year
                if (year < 2020 || year > 2100) {
                    exceptionMessages.put(rowNumber, "Year must be between 2020 and 2100!");
                    checkValidation = false;
                }

                //add new transaction data row
                if (checkValidation) {
                    Set<Integer> salaryGrossIdsAsFinal;

                    //convert from string of salary gross ids to set of integer ids
                    if (salaryGrossIds.isEmpty() || salaryGrossIds.isBlank()) {
                        salaryGrossIdsAsFinal = Collections.emptySet();
                    } else {
                        List<String> salaryGrossIdsAsString = new ArrayList<>(
                                Arrays.asList(salaryGrossIds.split(","))
                        );

                        salaryGrossIdsAsFinal = salaryGrossIdsAsString.stream()
                                .map(Integer::parseInt)
                                .collect(Collectors.toSet());
                    }

                    transactionData.add(TransactionSalaryPaymentDto.builder()
                            .employeeUuid(employeeUuid)
                            .month(month)
                            .year(year)
                            .salaryGrossIds(salaryGrossIdsAsFinal)
                            .build());
                }
            } catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
            }
        }

        //check if there are any exceptions
        if (exceptionMessages.isEmpty()) {
            //perform transactions here

            //check a valid transaction
            transactionData.forEach(this::checkValidTransaction);

            //perform each transaction against the database
            transactionData.forEach(this::createNewSalaryPayment);

            return Map.of(1, "Upload Salary Slip Successfully!");
        }

        return exceptionMessages;
    }

    private void checkValidTransaction(TransactionSalaryPaymentDto transactionSalaryPaymentDto) {
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
