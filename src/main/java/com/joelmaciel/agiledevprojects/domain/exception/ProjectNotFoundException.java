package com.joelmaciel.agiledevprojects.domain.exception;

public class ProjectNotFoundException extends EntityNotExistException{
    public ProjectNotFoundException(String message) {
        super(message);
    }

    public ProjectNotFoundException(Long companyId) {
        this(String.format("There is no project with this id %d saved in the database", companyId));
    }
}
