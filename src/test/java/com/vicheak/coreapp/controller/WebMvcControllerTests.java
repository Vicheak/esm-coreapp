package com.vicheak.coreapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vicheak.coreapp.api.auth.AuthService;
import com.vicheak.coreapp.api.department.DepartmentService;
import com.vicheak.coreapp.api.department.web.DepartmentDto;
import com.vicheak.coreapp.api.employee.BaseSalaryLogService;
import com.vicheak.coreapp.api.employee.EmployeeService;
import com.vicheak.coreapp.api.file.FileService;
import com.vicheak.coreapp.api.report.ReportService;
import com.vicheak.coreapp.api.salarygross.GrossTypeService;
import com.vicheak.coreapp.api.salarygross.SalaryGrossService;
import com.vicheak.coreapp.api.slip.PaymentStateService;
import com.vicheak.coreapp.api.slip.SalaryPaymentService;
import com.vicheak.coreapp.api.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class WebMvcControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentService departmentService;
    @MockBean
    private EmployeeService employeeService;
    @MockBean
    private BaseSalaryLogService baseSalaryLogService;
    @MockBean
    private FileService fileService;
    @MockBean
    private ReportService reportService;
    @MockBean
    private GrossTypeService grossTypeService;
    @MockBean
    private SalaryGrossService salaryGrossService;
    @MockBean
    private PaymentStateService paymentStateService;
    @MockBean
    private SalaryPaymentService salaryPaymentService;
    @MockBean
    private AuthService authService;
    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testWebDepartmentControllerLoadAllDepartments() throws Exception {
        //given
        List<DepartmentDto> departmentDtoList = new ArrayList<>() {{
            add(new DepartmentDto("IT",
                    "Information Technology",
                    "12345610453"));
            add(new DepartmentDto("Graphics Design",
                    "Design Posters, Banners,...",
                    "12345831671"));
        }};

        //mock service behavior
        given(departmentService.loadAllDepartments())
                .willReturn(departmentDtoList);

        //when
        ResultActions response = mockMvc.perform(get("/api/v1/departments"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(departmentDtoList.size())));
    }

    @Test
    public void testWebDepartmentControllerLoadAllDepartmentsEmpty() throws Exception {
        //given
        given(departmentService.loadAllDepartments())
                .willReturn(Collections.emptyList());

        //when
        ResultActions response = mockMvc.perform(get("/api/v1/departments"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    public void testWebDepartmentControllerLoadDepartmentByName() throws Exception {
        //given
        DepartmentDto departmentDto = new DepartmentDto("IT",
                "Information Technology",
                "12345610453");
        given(departmentService.loadDepartmentByName("IT"))
                .willReturn(departmentDto);

        //when
        ResultActions response = mockMvc.perform(get("/api/v1/departments/{name}",
                departmentDto.departmentName()));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departmentName", is(departmentDto.departmentName())))
                .andExpect(jsonPath("$.departmentDescription", is(departmentDto.departmentDescription())))
                .andExpect(jsonPath("$.departmentPhone", is(departmentDto.departmentPhone())));
    }

    @Test
    public void testWebDepartmentControllerLoadDepartmentByNameEmpty() throws Exception {
        //given
        given(departmentService.loadDepartmentByName("IT"))
                .willReturn(any());

        //when
        ResultActions response = mockMvc.perform(get("/api/v1/departments/{name}", "IT"));

        //then
        response.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testWebDepartmentControllerCreateNewDepartment() throws Exception {
        //given
        DepartmentDto departmentDto = new DepartmentDto("IT",
                "Information Technology",
                "12345610453");

        //return type as void -> no answer from mocking
        willDoNothing().given(departmentService).createNewDepartment(departmentDto);

        //when
        ResultActions response = mockMvc.perform(post("/api/v1/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(departmentDto)));

        //then
        response.andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void testWebDepartmentControllerDeleteDepartmentByName() throws Exception {
        //given
        willDoNothing().given(departmentService).deleteDepartmentByName("IT");

        //when
        ResultActions response =
                mockMvc.perform(delete("/api/v1/departments/{name}", "IT"));

        //then
        response.andDo(print())
                .andExpect(status().isNoContent());
    }

}
