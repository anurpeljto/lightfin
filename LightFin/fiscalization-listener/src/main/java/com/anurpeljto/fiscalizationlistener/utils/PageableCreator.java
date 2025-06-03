package com.anurpeljto.fiscalizationlistener.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public class PageableCreator {

    public static Pageable createPageable(Integer page, Integer size, String filterBy, String sortBy) {
        String sortField = (filterBy == null || filterBy.isBlank() || filterBy.equalsIgnoreCase("null")) ? "id" : filterBy;

        Sort.Direction direction;
        try {
            direction = Sort.Direction.valueOf(
                    (sortBy == null || sortBy.isBlank() || sortBy.equalsIgnoreCase("null")) ? "DESC" : sortBy.toUpperCase()
            );
        } catch (IllegalArgumentException e) {
            direction = Sort.Direction.DESC;
        }

        return PageRequest.of(
                page == null ? 0 : page,
                size == null ? 10 : size,
                Sort.by(direction, sortField)
        );
    }

}
