package com.vicheak.coreapp.service.department;

import com.vicheak.coreapp.api.department.*;
import com.vicheak.coreapp.api.department.web.DepartmentDto;
import com.vicheak.coreapp.util.FormatUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTests {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department department;
    private DepartmentDto departmentDto;

    @BeforeEach
    public void setup() {
        department = Department.builder()
                .id(1L)
                .name("IT")
                .description("Information Technology")
                .phone("12345610453")
                .build();

        departmentDto = new DepartmentDto("IT",
                "Information Technology",
                "12345610453");
    }

    @Test
    public void testLoadAllDepartments() {
        //given
        //mock repository behavior
        given(departmentRepository.findAll())
                .willReturn(List.of(department));

        //mock mapper behavior
        given(departmentMapper.toDepartmentDto(anyList()))
                .willReturn(List.of(departmentDto));

        //when
        List<DepartmentDto> departmentDtoList =
                departmentService.loadAllDepartments();

        //then
        assertThat(departmentDtoList).isNotNull();
        assertThat(departmentDtoList).isNotEmpty();
        assertEquals(departmentDtoList.size(), 1);
    }

    @Test
    public void testLoadAllDepartmentsEmpty() {
        //given
        //mock repository behavior
        given(departmentRepository.findAll())
                .willReturn(Collections.emptyList());

        //mock mapper behavior
        given(departmentMapper.toDepartmentDto(anyList()))
                .willReturn(Collections.emptyList());

        //when
        List<DepartmentDto> departmentDtoList =
                departmentService.loadAllDepartments();

        //then
        assertThat(departmentDtoList).isEmpty();
    }

    @Test
    public void testLoadDepartmentByName() {
        //given
        given(departmentRepository.findByNameIgnoreCase("IT"))
                .willReturn(Optional.of(department));

        given(departmentMapper.toDepartmentDto(department))
                .willReturn(departmentDto);

        //when
        DepartmentDto departmentDto = departmentService.loadDepartmentByName("IT");


        //then
        assertThat(departmentDto).isNotNull();
        assertEquals(departmentDto.departmentName(), "IT");
    }

    @Test
    public void testLoadDepartmentByNameThrow() {
        //given
        given(departmentRepository.findByNameIgnoreCase("its"))
                .willReturn(Optional.empty());

        //when
        assertThrows(ResponseStatusException.class,
                () -> departmentService.loadDepartmentByName("its"));

        //then
    }

    @Test
    public void testCreateNewDepartment() {
        //given
        //mock all the logic
        Boolean isChecked = FormatUtil.checkPhoneFormat("12345610453");

        given(departmentRepository.existsByNameIgnoreCaseOrPhone("IT", "12345610453"))
                .willReturn(false);

        given(departmentMapper.fromDepartmentDto(departmentDto))
                .willReturn(department);

        given(departmentRepository.save(department))
                .willReturn(department);

        //when
        departmentService.createNewDepartment(departmentDto);

        //then
        assertThat(isChecked).isTrue();
        assertThat(department).isNotNull();
    }

    @Test
    public void testCreateNewDepartmentThrowCheckPhoneFormat(){
        //given
        departmentDto = new DepartmentDto("IT",
                "Information Technology",
                "12345610453f");
        Boolean isChecked = FormatUtil.checkPhoneFormat("12345610453f");

        //when
        assertThrows(ResponseStatusException.class,
                () -> departmentService.createNewDepartment(departmentDto));

        //then
        assertThat(isChecked).isFalse();
        verify(departmentMapper, never()).fromDepartmentDto(departmentDto);
        verify(departmentRepository, never()).save(department);
    }

    @Test
    public void testCreateNewDepartmentThrowConflictResource() {
        //given
        Boolean isChecked = FormatUtil.checkPhoneFormat("12345610453");

        given(departmentRepository.existsByNameIgnoreCaseOrPhone("IT", "12345610453"))
                .willReturn(true);

        //when
        assertThrows(ResponseStatusException.class,
                () -> departmentService.createNewDepartment(departmentDto));

        //then
        assertThat(isChecked).isTrue();
        verify(departmentMapper, never()).fromDepartmentDto(departmentDto);
        verify(departmentRepository, never()).save(department);
    }

}
