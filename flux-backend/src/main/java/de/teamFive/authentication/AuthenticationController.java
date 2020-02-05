package de.teamFive.authentication;

import de.teamFive.common.user.User;
import de.teamFive.session.SessionService;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Path("authentication")
public class AuthenticationController {
    private static final Logger log = Logger.getLogger(AuthenticationController.class.getName());
    @EJB
    private AuthenticationService authService;
    @EJB
    private SessionService sessionService;
    @Context
    private HttpServletRequest request;

    /**
     * Registers a new user using the given {@link LoginDTO}
     *
     * @param loginDTO holding the necessary information to register a user
     * @return a {@link Response} signaling that the registration was successful by sending a 200(OK) http status code
     */
    @POST
    @Path("/register")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response register(LoginDTO loginDTO) {
        authService.register(loginDTO.getUsername(), loginDTO.getPassword());
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Logs in a user by validating the given credentials using the {@link AuthenticationService#authenticate(String, String) authenticate} method.
     * Additionally the current {@link de.teamFive.session.Session} will be checked to see if a user is already associated with that session.
     *
     * @param loginDTO
     * @return a {@link Response} which will signal either a successful login (200(OK)) or a failed login (400(Bad Request))
     */
    @POST
    @Path("/login")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response login(LoginDTO loginDTO) {
        User user = authService.authenticate(loginDTO.getUsername(), loginDTO.getPassword());
        if (!sessionService.doesExist(request.getSession().getId())) {
            sessionService.associateUser(request.getSession().getId(), user);
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).header("reason", "Session already bound to user!").build();
    }

    /**
     * Logs out user by removing him from the current {@link de.teamFive.session.Session}
     *
     * @return a {@link Response} signaling that the logout was successful by sending a 200(OK) http status code
     */
    @POST
    @Path("/logout")
    public Response logout() {
        sessionService.removeSession(request.getSession().getId());
        return Response.status(Response.Status.OK).build();
    }
}
