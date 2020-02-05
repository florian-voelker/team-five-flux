package de.teamFive.common.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class InvalidUsernameOrPasswordExceptionMapper implements ExceptionMapper<InvalidUsernameOrPasswordException> {
    private static final Logger log = Logger.getLogger(InvalidUsernameOrPasswordExceptionMapper.class.getName());

    @Override
    public Response toResponse(InvalidUsernameOrPasswordException exception) {
        log.log(Level.WARNING, exception.getMessage());
        return Response.status(Response.Status.UNAUTHORIZED)
                .header("reason", exception.getMessage())
                .build();
    }
}
