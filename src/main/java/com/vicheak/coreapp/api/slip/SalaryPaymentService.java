package com.vicheak.coreapp.api.slip;

import com.vicheak.coreapp.api.slip.web.TransactionSalaryPaymentDto;
import com.vicheak.coreapp.api.slip.web.UpdatePaymentStateDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface SalaryPaymentService {

    /**
     * This method is used to create new salary slip for specific employee
     * ,including choosing specific employee to get slip and generating salary slip (benefit and deduction)
     * @param transactionSalaryPaymentDto is the request from client followed by DTO pattern
     */
    void createNewSalaryPayment(TransactionSalaryPaymentDto transactionSalaryPaymentDto);

    /**
     * This method is used to create new salary payment resources and the related entities' resources from
     * Microsoft Excel file (.xls or .xlsx)<br>
     * Sample Related Data In Excel file (SheetName : salary_slip_sheet)<br>
     ****** Employee UUID&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Month
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Year
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Salary Gross IDs<br>
     ****** 3d77cbbc-068b-4ae9-a397-2ec01b2428c4
     * &nbsp;&nbsp;&nbsp;&nbsp;12
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2023<br>
     ****** 97dfd3e6-b38c-478c-bc3f-79df8c2753ef
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;11
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2023
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1,2<br>
     ****** 4b825c31-5e7c-4ee7-9da5-6b8325f404c9
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;11
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2023
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3,4,5<br>
     * @param excelFile is the request part file from client
     * @return Map<Integer, String>
     * @throws IOException throws IO Exception to client
     */
    Map<Integer, String> uploadSalaryPayments(MultipartFile excelFile);

    /**
     * This method is used to update payment status of specific salary payment
     * @param uuid is the path parameter from client
     * @param updatePaymentStateDto is the request from client
     */
    void updatePaymentStateByUuid(String uuid, UpdatePaymentStateDto updatePaymentStateDto);

    /**
     * This method is used to delete specific salary payment by uuid
     * @param uuid is the path parameter from client
     */
    void deleteSalaryPaymentByUuid(String uuid);

}
