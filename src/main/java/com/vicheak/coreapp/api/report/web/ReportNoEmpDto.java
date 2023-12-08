package com.vicheak.coreapp.api.report.web;

import lombok.Builder;

@Builder
public record ReportNoEmpDto(String departmentName,

                             Integer numberOfEmployee) {
}
