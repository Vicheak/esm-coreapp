package com.vicheak.coreapp.pagination;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageDto {

    private List<?> list;
    private PaginationDto pagination;

    public PageDto(List<?> list, Page<?> page) {
        //return content of the page
        this.list = list;

        //build some common pagination info
        this.pagination = PaginationDto.builder()
                .pageNumber(page.getPageable().getPageNumber() + 1)
                .pageSize(page.getPageable().getPageSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .numberOfElements(page.getNumberOfElements())
                .first(page.isFirst())
                .last(page.isLast())
                .empty(page.isEmpty())
                .build();
    }

}
