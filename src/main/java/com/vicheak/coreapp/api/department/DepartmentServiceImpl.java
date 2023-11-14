package com.vicheak.coreapp.api.department;

import com.vicheak.coreapp.api.department.web.DepartmentDto;
import com.vicheak.coreapp.spec.DepartmentFilter;
import com.vicheak.coreapp.spec.DepartmentSpec;
import com.vicheak.coreapp.util.FormatUtil;
import com.vicheak.coreapp.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public List<DepartmentDto> loadAllDepartments() {
        return departmentMapper.toDepartmentDto(departmentRepository.findAll());
    }

    @Override
    public DepartmentDto loadDepartmentByName(String name) {
        Department department = departmentRepository.findByNameIgnoreCase(name)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                String.format("Department with name = %s not found...!", name))
                );
        return departmentMapper.toDepartmentDto(department);
    }

    @Override
    public List<DepartmentDto> searchDepartments(Map<String, String> requestMap) {
        //extract the data from request map
        String name = "";
        String phone = "";

        if (requestMap.containsKey(Department_.NAME))
            name = requestMap.get(Department_.NAME);

        if (requestMap.containsKey(Department_.PHONE))
            phone = requestMap.get(Department_.PHONE);

        //using jpa specification to build dynamic query
        List<Department> departments = departmentRepository.findAll(DepartmentSpec.builder()
                .departmentFilter(DepartmentFilter.builder()
                        .name(name)
                        .phone(phone)
                        .build())
                .build());

        return departmentMapper.toDepartmentDto(departments);
    }

    @Override
    public void createNewDepartment(DepartmentDto departmentDto) {
        //check if department phone is correct format (all are digits)
        if (!FormatUtil.checkPhoneFormat(departmentDto.departmentPhone()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Department phone is not in correct format!");

        //check if department name or phone already exists
        if (departmentRepository.existsByNameIgnoreCaseOrPhone(departmentDto.departmentName(), departmentDto.departmentPhone()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Department name or phone conflicts resources in the system!");

        Department department = departmentMapper.fromDepartmentDto(departmentDto);

        //add more six random numbers
        department.setPhone(department.getPhone() + RandomUtil.getRandomNumber());

        departmentRepository.save(department);
    }

    @Override
    public void updateDepartmentByName(String name, DepartmentDto departmentDto) {
        boolean isPhoneCheck = false;

        //load department by name
        Department department = departmentRepository.findByNameIgnoreCase(name)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                String.format("Department with name = %s not found...!", name))
                );

        //check if department name already exists
        if (Objects.nonNull(departmentDto.departmentName()))
            if (!departmentDto.departmentName().equalsIgnoreCase(department.getName()) &&
                    departmentRepository.existsByNameIgnoreCase(departmentDto.departmentName()))
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Department name conflicts resources in the system!");

        if (Objects.nonNull(departmentDto.departmentPhone())) {
            //check if department phone is correct format (all are digits)
            if (!FormatUtil.checkPhoneFormat(departmentDto.departmentPhone()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Department phone is not in correct format!");

            //check if department phone already exists
            if (!departmentDto.departmentPhone().equals(department.getPhone()) &&
                    departmentRepository.existsByPhone(departmentDto.departmentPhone()))
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Department phone conflicts resources in the system!");

            if (!departmentDto.departmentPhone().equals(department.getPhone()))
                isPhoneCheck = true;
        }

        //map non-null dto to entity
        departmentMapper.fromDepartmentDto(department, departmentDto);

        if (isPhoneCheck)
            //add more six random numbers
            department.setPhone(departmentDto.departmentPhone() + RandomUtil.getRandomNumber());

        departmentRepository.save(department);
    }

    @Override
    public void deleteDepartmentByName(String name) {
        Department department = departmentRepository.findByNameIgnoreCase(name)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                String.format("Department with name = %s not found...!", name))
                );

        departmentRepository.delete(department);
    }

}
