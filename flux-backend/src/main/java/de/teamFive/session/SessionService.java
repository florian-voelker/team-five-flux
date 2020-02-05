package de.teamFive.session;

import de.teamFive.common.exception.SessionNotFoundException;
import de.teamFive.common.user.User;
import de.teamFive.savegame.Savegame;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Stateless
public class SessionService {
    private final Logger logger = Logger.getLogger(SessionService.class.getCanonicalName());

    @EJB
    private SessionDAO sessionDAO;

    /**
     * Links the given {@link User} with the identified {@link Session} and updates the session accordingly.
     *
     * @param id   of the {@link Session} which should be updated
     * @param user to be associated with the {@link Session}
     */
    public void associateUser(String id, User user) {
        Session session = sessionDAO.save(new Session(id, user, new ArrayList<>()));
        session.setUser(user);
        sessionDAO.update(session);
    }

    /**
     * Retrieves the {@link User} which is linked to the given {@link Session}
     *
     * @param id of the {@link Session} whose user should be returned
     * @return the {@link User} which is associated with the given session
     */
    public User associatedUser(String id) {
        Session session = sessionDAO.findBySessionId(id).orElseThrow(SessionNotFoundException::new);
        return session.getUser();
    }

    /**
     * Removes the given session from the repository
     *
     * @param id of the session
     */
    public void removeSession(String id) {
        Session session = sessionDAO.findBySessionId(id).orElseThrow(SessionNotFoundException::new);
        sessionDAO.delete(session);
    }

    /**
     * Checks if a given {@link Session} exists
     *
     * @param id of the session
     * @return boolean signaling whether or not the {@link Session} exists
     */
    public boolean doesExist(String id) {
        Optional<Session> sessionOptional = sessionDAO.findBySessionId(id);
        return sessionOptional.isPresent();
    }

    /**
     * Replaces the existing gamestate in given {@link Session} with the new provided gamestate
     *
     * @param id        of the session
     * @param gamestate replacing the old gamestate
     */
    public void setGameState(String id, List<Integer> gamestate) {
        Session session = sessionDAO.findBySessionId(id).orElseThrow(SessionNotFoundException::new);
        session.setGamestate(gamestate);
        sessionDAO.update(session);
    }

    /**
     * Retrieves the current gamestate held in the {@link Session}
     *
     * @param id of the session
     * @return a integer-arraylist holding the gamestate
     */
    public List<Integer> getGameState(String id) {
        Session session = sessionDAO.findBySessionId(id).orElseThrow(SessionNotFoundException::new);
        return session.getGamestate();
    }

    /**
     * Retrieves all savegames held by the {@link User} currently associated with the given {@link Session}
     *
     * @param id of the session
     * @return a arraylist containing all {@link Savegame}'s held by the user
     */
    public List<Savegame> getAllSavegamesFromUser(String id) {
        User user = associatedUser(id);
        return user.getSavegames();
    }
}
