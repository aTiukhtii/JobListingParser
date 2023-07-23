package com.example.testtaskdataox.util;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class ParsingJobListing {
    private static final String SEMICOLON = ";";
    private static final String DOUBLE_DOTS = ":";
    private static final Integer CONSTANT_FOR_COLUMN_NAME = 0;
    private static final Integer CONSTANT_FOR_DIRECTION = 1;

    public static List<Sort.Order> parsing(String sortBy) {
        List<Sort.Order> orders = new ArrayList<>();
        if (sortBy.contains(DOUBLE_DOTS)) {
            String[] sortingFields = sortBy.split(SEMICOLON);
            for (String field : sortingFields) {
                Sort.Order order;
                if (field.contains(DOUBLE_DOTS)) {
                    String[] fieldsAndDirections = field.split(DOUBLE_DOTS);
                    order = new Sort.Order(Sort.Direction.valueOf(
                            fieldsAndDirections[CONSTANT_FOR_DIRECTION]),
                            fieldsAndDirections[CONSTANT_FOR_COLUMN_NAME]);
                } else {
                    order = new Sort.Order(Sort.Direction.DESC, field);
                }
                orders.add(order);
            }
        } else {
            Sort.Order order = new Sort.Order(Sort.Direction.ASC, sortBy);
            orders.add(order);
        }
        return orders;
    }
}
