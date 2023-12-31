package com.joelmaciel.agiledevprojects.domain.exception;

public abstract class EntityNotExistException extends BusinessException {

    public EntityNotExistException(String message) {
        super(message);
    }
}
