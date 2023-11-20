package com.vicheak.coreapp.api.salarygross.web;

import com.vicheak.coreapp.api.salarygross.SalaryGrossService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/salaryGross")
@RequiredArgsConstructor
public class SalaryGrossController {

    private final SalaryGrossService salaryGrossService;

    @GetMapping
    public List<SalaryGrossDto> loadAllSalaryGross() {
        return salaryGrossService.loadAllSalaryGross();
    }

    @GetMapping("/{name}")
    public SalaryGrossDto loadSalaryGrossByName(@PathVariable("name") String name) {
        return salaryGrossService.loadSalaryGrossByName(name);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createNewSalaryGross(@RequestBody @Valid TransactionSalaryGrossDto transactionSalaryGrossDto){
        salaryGrossService.createNewSalaryGross(transactionSalaryGrossDto);
    }

}
