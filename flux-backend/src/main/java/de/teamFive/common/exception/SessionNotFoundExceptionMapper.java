package de.teamFive.common.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SessionNotFoundExceptionMapper implements ExceptionMapper<SessionNotFoundException> {
    private static final Logger log = Logger.getLogger(SessionNotFoundExceptionMapper.class.getCanonicalName());

    @Override
    public Response toResponse(SessionNotFoundException exception) {
        log.log(Level.WARNING, exception.getMessage());
        return Response.status(Response.Status.BAD_REQUEST)
                .header("reason", exception.getMessage())
                .build();
    }
}
