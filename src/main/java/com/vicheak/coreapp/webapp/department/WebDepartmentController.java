package com.vicheak.coreapp.webapp.department;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/departments")
@RequiredArgsConstructor
public class WebDepartmentController {

    private final WebDepartmentService webDepartmentService;

    @GetMapping
    public String findAllDepartments(ModelMap modelMap){
        modelMap.addAttribute("departments",
                webDepartmentService.loadAllDepartments());
        return "department/main";
    }

}
