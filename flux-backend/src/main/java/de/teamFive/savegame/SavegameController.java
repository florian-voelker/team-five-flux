package de.teamFive.savegame;

import de.teamFive.common.user.User;
import de.teamFive.session.Session;
import de.teamFive.session.SessionService;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Path("savegame")
public class SavegameController {
    @Context
    private HttpServletRequest request;
    @EJB
    private SessionService sessionService;
    @EJB
    private SavegameService savegameService;

    /**
     * Generates an integer-arraylist only holding one entry, which is a zero, representing a new game.
     * This arraylist will be saved in the current {@link Session}
     */
    @GET
    @Path("new-game")
    public Response newGame() {
        List<Integer> gamestate = new ArrayList<>();
        gamestate.add(1);
        sessionService.setGameState(request.getSession().getId(), gamestate);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Saves the game progress, which is held in the current {@link Session},
     * into the corresponding repository.
     *
     * @param name
     */
    @PUT
    @Path("save")
    @Consumes({MediaType.APPLICATION_JSON})
    public void save(@QueryParam("name") String name) {
        String sessionId = request.getSession().getId();
        List<Integer> gamestate = new ArrayList<>(sessionService.getGameState(sessionId));
        User user = sessionService.associatedUser(sessionId);
        Long timestamp = new Date().getTime();

        savegameService.save(user, timestamp, name, gamestate);
    }

    /**
     * Loads a {@link Savegame}, identified by the given savegameId, into the {@link Session}.
     *
     * @param savegameId identifying the savegame to be loaded
     * @return a {@link SavegameDTO} holding the essential information regarding a savegame
     */
    @PUT
    @Path("load")
    @Produces(MediaType.APPLICATION_JSON)
    public SavegameDTO load(@QueryParam("savegame-id") Long savegameId) {
        String sessionId = request.getSession().getId();
        User user = sessionService.associatedUser(sessionId);
        Savegame newGamestate = savegameService.load(user, savegameId);

        sessionService.setGameState(sessionId, new ArrayList<>(newGamestate.getActivities()));
        return new SavegameDTO(newGamestate.getId(), newGamestate.getTimestamp(), newGamestate.getName(), new ArrayList<>(newGamestate.getActivities()));
    }

    /**
     * Returns all savegames held by the currently logged in user.
     *
     * @return a list of {@link SavegameDTO}'s
     */
    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_JSON})
    public List<SavegameDTO> getAllSavegames() {
        List<Savegame> saves = sessionService.getAllSavegamesFromUser(request.getSession().getId());
        List<SavegameDTO> saveDTOs = new ArrayList<>();
        for (Savegame save : saves) {
            saveDTOs.add(new SavegameDTO(save.getId(), save.getTimestamp(), save.getName(), save.getActivities()));
        }
        return saveDTOs;
    }

}
