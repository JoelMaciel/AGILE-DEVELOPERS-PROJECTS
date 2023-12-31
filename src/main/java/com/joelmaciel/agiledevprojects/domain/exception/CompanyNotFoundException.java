package com.joelmaciel.agiledevprojects.domain.exception;

public class CompanyNotFoundException extends EntityNotExistException {
    public CompanyNotFoundException(String message) {
        super(message);
    }

    public CompanyNotFoundException(Long companyId) {
        this(String.format("There is no company with this id %d saved in the database", companyId));
    }
}
