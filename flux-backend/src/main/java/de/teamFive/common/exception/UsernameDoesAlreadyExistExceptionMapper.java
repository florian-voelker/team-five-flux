package de.teamFive.common.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class UsernameDoesAlreadyExistExceptionMapper implements ExceptionMapper<UsernameDoesAlreadyExistException> {
    private static final Logger log = Logger.getLogger(UsernameDoesAlreadyExistExceptionMapper.class.getName());

    @Override
    public Response toResponse(UsernameDoesAlreadyExistException exception) {
        log.log(Level.WARNING, exception.getMessage());

        return Response.status(Response.Status.BAD_REQUEST)
                .header("reason", exception.getMessage())
                .build();
    }
}
