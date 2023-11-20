package com.vicheak.coreapp.api.salarygross;

import com.vicheak.coreapp.api.salarygross.web.SalaryGrossDto;
import com.vicheak.coreapp.api.salarygross.web.TransactionSalaryGrossDto;

import java.util.List;

public interface SalaryGrossService {

    /**
     * This method is used to load all salary gross in the system
     * @return List<SalaryGrossDto>
     */
    List<SalaryGrossDto> loadAllSalaryGross();

    /**
     * This method is used to load specific salary gross by name
     * @param name is the path parameter from client
     * @return SalaryGrossDto
     */
    SalaryGrossDto loadSalaryGrossByName(String name);

    /**
     * This method is used to create new salary gross resource
     * @param transactionSalaryGrossDto is the request from client
     */
    void createNewSalaryGross(TransactionSalaryGrossDto transactionSalaryGrossDto);

    /**
     * This method is used to update specific salary gross resource by name
     * @param name is the path parameter from client
     * @param transactionSalaryGrossDto is the request from client
     */
    void updateSalaryGrossByName(String name,TransactionSalaryGrossDto transactionSalaryGrossDto);

    /**
     * This method is used to delete specific salary gross by name
     * @param name is the path parameter from client
     */
    void deleteSalaryGrossByName(String name);

}
