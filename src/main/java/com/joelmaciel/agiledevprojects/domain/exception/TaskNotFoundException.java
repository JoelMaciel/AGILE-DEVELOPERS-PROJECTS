package com.joelmaciel.agiledevprojects.domain.exception;

public class TaskNotFoundException extends EntityNotExistException {
    public TaskNotFoundException(String message) {
        super(message);
    }

    public TaskNotFoundException(Long taskId) {
        this(String.format("There is no task with this id %d saved in the database", taskId));
    }
}
