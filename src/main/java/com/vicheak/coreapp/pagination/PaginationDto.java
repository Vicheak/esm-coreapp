package com.vicheak.coreapp.pagination;

import lombok.Builder;

@Builder
public record PaginationDto(int pageNumber,
                            int pageSize,
                            int totalPages,
                            long totalElements,
                            int numberOfElements,
                            boolean first,
                            boolean last,
                            boolean empty) {
}