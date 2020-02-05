package de.teamFive.savegame;

import de.teamFive.common.user.User;
import de.teamFive.common.user.UserDAO;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.List;

@Stateless
public class SavegameService {
    @Context
    private HttpServletRequest request;
    @EJB
    private SavegameDAO savegameDAO;
    @EJB
    private UserDAO userDAO;

    /**
     * Saves the given {@link Savegame} in the corresponding repository and adds it to the given {@link User} which will
     * be updated accordingly.
     *
     * @param user      which should be associated with the savegame
     * @param timestamp at which the game was saved
     * @param name      the name which was used during the saving process
     * @param activities progress in the game
     */
    public void save(User user, Long timestamp, String name, List<Integer> activities) {
        Savegame savegame = savegameDAO.save(new Savegame(null, timestamp, name, activities));
        user.getSavegames().add(savegame);
        userDAO.update(user);
    }

    /**
     * Loads a {@link Savegame} by retrieving all savegames held by the {@link User} and searching for a savegame which
     * corresponds to the given savegameId.
     *
     * @param user       holding all savegames
     * @param savegameId identifying the savegame to load
     * @return a savegame matching the savegameId
     */
    public Savegame load(User user, Long savegameId) {
        Savegame searchedSave = null;

        for (Savegame savegame : user.getSavegames()) {
            if (savegame.getId().equals(savegameId)) {
                searchedSave = savegame;
                break;
            }
        }
        if (searchedSave == null) {
            throw new RuntimeException("Couldn't find a savegame with id " + savegameId);
        }

        return searchedSave;
    }
}
