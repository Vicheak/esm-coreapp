package com.vicheak.coreapp.service.department;

import com.vicheak.coreapp.api.department.*;
import com.vicheak.coreapp.api.department.web.DepartmentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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

}
