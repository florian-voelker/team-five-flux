package de.teamFive.common.exception;

public class InvalidUsernameOrPasswordException extends RuntimeException {
    public InvalidUsernameOrPasswordException(){
        super("Wrong password or username!");
    }
}
