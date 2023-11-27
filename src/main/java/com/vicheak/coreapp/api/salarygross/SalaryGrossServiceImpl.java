package com.vicheak.coreapp.api.salarygross;

import com.vicheak.coreapp.api.salarygross.web.SalaryGrossDto;
import com.vicheak.coreapp.api.salarygross.web.TransactionSalaryGrossDto;
import com.vicheak.coreapp.spec.SalaryGrossFilter;
import com.vicheak.coreapp.spec.SalaryGrossSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SalaryGrossServiceImpl implements SalaryGrossService {

    private final SalaryGrossRepository salaryGrossRepository;
    private final SalaryGrossMapper salaryGrossMapper;
    private final GrossTypeRepository grossTypeRepository;

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
    public List<SalaryGrossDto> searchSalaryGross(Map<String, String> requestMap) {
        //extract the data from request map
        String name = "";
        BigDecimal amount = null;

        if (requestMap.containsKey(SalaryGross_.NAME))
            name = requestMap.get(SalaryGross_.NAME);

        if (requestMap.containsKey(SalaryGross_.AMOUNT))
            amount = new BigDecimal(requestMap.get(SalaryGross_.AMOUNT));

        //using jpa specification to build dynamic query
        List<SalaryGross> salaryGrosses = salaryGrossRepository.findAll(SalaryGrossSpec.builder()
                .salaryGrossFilter(SalaryGrossFilter.builder()
                        .name(name)
                        .amount(amount)
                        .build())
                .build());

        return salaryGrossMapper.toSalaryGrossDto(salaryGrosses);
    }

    @Override
    public void createNewSalaryGross(TransactionSalaryGrossDto transactionSalaryGrossDto) {
        //check if salary gross name already exists
        if (salaryGrossRepository.existsByNameIgnoreCase(transactionSalaryGrossDto.name()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Salary gross name conflicts resources in the system!");

        //map from transaction salary gross dto to entity
        SalaryGross salaryGross = salaryGrossMapper.fromTransactionSalaryGrossDto(transactionSalaryGrossDto);

        //save salary gross into the database
        salaryGrossRepository.save(salaryGross);
    }

    @Override
    public void updateSalaryGrossByName(String name, TransactionSalaryGrossDto transactionSalaryGrossDto) {
        //load salary gross from the database
        SalaryGross salaryGross = salaryGrossRepository.findByNameIgnoreCase(name)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Salary Gross with name = %s has not been found...!".formatted(name))
                );

        //check if salary gross name already exists
        if (Objects.nonNull(transactionSalaryGrossDto.name()))
            if (!transactionSalaryGrossDto.name().equalsIgnoreCase(salaryGross.getName()) &&
                    salaryGrossRepository.existsByNameIgnoreCase(transactionSalaryGrossDto.name()))
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Salary gross name conflicts resources in the system!");

        //map from transaction salary gross dto to entity (avoid null value from dto)
        salaryGrossMapper.fromTransactionSalaryGrossDto(salaryGross, transactionSalaryGrossDto);

        //save salary gross into the database
        salaryGrossRepository.save(salaryGross);
    }

    @Override
    public void deleteSalaryGrossByName(String name) {
        //load salary gross from the database
        SalaryGross salaryGross = salaryGrossRepository.findByNameIgnoreCase(name)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Salary Gross with name = %s has not been found...!".formatted(name))
                );

        salaryGrossRepository.delete(salaryGross);
    }

    @Override
    public List<SalaryGrossDto> loadSalaryGrossByGrossTypeName(String name) {
        Optional<GrossType> grossType = grossTypeRepository.findByNameIgnoreCase(name);
        if (grossType.isEmpty()) return Collections.emptyList(); //return empty list to client

        return salaryGrossMapper.toSalaryGrossDto(salaryGrossRepository.findByGrossTypeId(grossType.get().getId()));
    }

}
