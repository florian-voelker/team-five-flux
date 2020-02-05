package de.teamFive.common.exception;

public class UsernameDoesAlreadyExistException extends RuntimeException {

    public UsernameDoesAlreadyExistException() {
        super("Username does already exist!");
    }
}
