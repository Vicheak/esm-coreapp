package com.vicheak.coreapp.api.salarygross;

import com.vicheak.coreapp.api.salarygross.web.SalaryGrossDto;
import com.vicheak.coreapp.api.salarygross.web.TransactionSalaryGrossDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalaryGrossServiceImpl implements SalaryGrossService {

    private final SalaryGrossRepository salaryGrossRepository;
    private final SalaryGrossMapper salaryGrossMapper;

    @Override
    public List<SalaryGrossDto> loadAllSalaryGross() {
        return salaryGrossMapper.toSalaryGrossDto(salaryGrossRepository.findAll());
    }

    @Override
    public SalaryGrossDto loadSalaryGrossByName(String name) {
        //load salary gross from the database
        SalaryGross salaryGross = salaryGrossRepository.findByNameIgnoreCase(name)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Salary Gross with name = %s has not been found...!".formatted(name))
                );

        return salaryGrossMapper.toSalaryGrossDto(salaryGross);
    }

    @Override
    public void createNewSalaryGross(TransactionSalaryGrossDto transactionSalaryGrossDto) {
        //check if salary gross name already exists
        if(salaryGrossRepository.existsByNameIgnoreCase(transactionSalaryGrossDto.name()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Salary gross name conflicts resources in the system!");

        //map from transaction salary gross dto to entity
        SalaryGross salaryGross = salaryGrossMapper.fromTransactionSalaryGrossDto(transactionSalaryGrossDto);

        //save salary gross into the database
        salaryGrossRepository.save(salaryGross);
    }

    @Override
    public void updateSalaryGrossByName(String name, TransactionSalaryGrossDto transactionSalaryGrossDto) {

    }

    @Override
    public void deleteSalaryGrossByName(String name) {

    }

}
