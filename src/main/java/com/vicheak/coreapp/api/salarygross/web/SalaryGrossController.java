package com.vicheak.coreapp.api.salarygross.web;

import com.vicheak.coreapp.api.salarygross.SalaryGrossService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/search")
    public List<SalaryGrossDto> searchSalaryGross(@RequestParam(required = false) Map<String, String> requestMap) {
        return salaryGrossService.searchSalaryGross(requestMap);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createNewSalaryGross(@RequestBody @Valid TransactionSalaryGrossDto transactionSalaryGrossDto) {
        salaryGrossService.createNewSalaryGross(transactionSalaryGrossDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{name}")
    public void updateSalaryGrossByName(@PathVariable("name") String name,
                                        @RequestBody TransactionSalaryGrossDto transactionSalaryGrossDto) {
        salaryGrossService.updateSalaryGrossByName(name, transactionSalaryGrossDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{name}")
    public void deleteSalaryGrossByName(@PathVariable("name") String name) {
        salaryGrossService.deleteSalaryGrossByName(name);
    }

}
