package com.joelmaciel.agiledevprojects.domain.exception;

public class DeveloperNotFoundException extends EntityNotExistException {
    public DeveloperNotFoundException(String message) {
        super(message);
    }

    public DeveloperNotFoundException(Long developerId) {
        this(String.format("There is no developer with this id %d saved in the database", developerId));
    }

}
