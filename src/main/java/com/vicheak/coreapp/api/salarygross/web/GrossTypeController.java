package com.vicheak.coreapp.api.salarygross.web;

import com.vicheak.coreapp.api.salarygross.GrossTypeService;
import com.vicheak.coreapp.api.salarygross.SalaryGrossService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/grossTypes")
@RequiredArgsConstructor
public class GrossTypeController {

    private final GrossTypeService grossTypeService;
    private final SalaryGrossService salaryGrossService;

    @GetMapping
    public List<GrossTypeDto> loadAllGrossTypes() {
        return grossTypeService.loadAllGrossTypes();
    }

    @GetMapping("/{name}")
    public GrossTypeDto loadGrossTypeByName(@PathVariable("name") String name) {
        return grossTypeService.loadGrossTypeByName(name);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createNewGrossType(@RequestBody @Valid GrossTypeDto grossTypeDto) {
        grossTypeService.createNewGrossType(grossTypeDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{name}")
    public void updateGrossTypeByName(@PathVariable("name") String name,
                                      @RequestBody GrossTypeDto grossTypeDto) {
        grossTypeService.updateGrossTypeByName(name, grossTypeDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{name}")
    public void deleteGrossTypeByName(@PathVariable("name") String name) {
        grossTypeService.deleteGrossTypeByName(name);
    }

    @GetMapping("/{name}/salaryGross")
    public List<SalaryGrossDto> loadSalaryGrossByGrossTypeName(@PathVariable("name") String name) {
        return salaryGrossService.loadSalaryGrossByGrossTypeName(name);
    }

}
