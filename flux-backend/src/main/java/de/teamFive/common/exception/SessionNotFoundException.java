package de.teamFive.common.exception;

public class SessionNotFoundException extends RuntimeException {
    public SessionNotFoundException(){
        super("Not session found for given sessionId!");
    }
}
