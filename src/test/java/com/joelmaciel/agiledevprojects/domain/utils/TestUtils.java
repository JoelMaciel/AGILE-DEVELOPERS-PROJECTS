package com.joelmaciel.agiledevprojects.domain.utils;

import com.joelmaciel.agiledevprojects.domain.entities.Company;

public class TestUtils {
    public static Company createCompany(String name) {
        return Company.builder()
                .name(name)
                .build();
    }
}
