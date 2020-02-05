package de.teamFive.common.services;

import de.teamFive.common.activities.ConnectedActivity;
import de.teamFive.common.entities.AvailableAction;
import de.teamFive.common.entities.CSMChoseOption;
import de.teamFive.common.entities.SSMGameState;
import de.teamFive.session.SessionService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This bean is responsible to handle all progress that has been made within the different playthroughs. Furthermore
 * it covers aspects like the validation of chosen actions.
 */
@Stateless
public class GameProgressHandlerBean {
    @EJB
    SessionService sessionServerice;
    @EJB
    AvailableActivityBean aab;
    private Logger logger = Logger.getLogger(GameProgressHandlerBean.class.getCanonicalName());

    /**
     * Utility method which resolves the ids of {@link ConnectedActivity}s with the {@link AvailableActivityBean}
     *
     * @param history the ids to be resolved
     * @returns the corresponding {@link ConnectedActivity}s
     */
    private List<ConnectedActivity> mapHistoryIntegerToCA(List<Integer> history) {
        return history.stream()
                .map(i -> aab.getSpecific(i).orElseThrow(() -> new RuntimeException("id " + i + " in history is unknown")))
                .collect(Collectors.toList());
    }

    /**
     * Ensures that an action can be taken when all its prerequisites have taken place
     *
     * @param history the history of the visited activities in the current playthrough
     * @param action  the action to be checked
     * @return whether this action can be undertaken
     */
    private boolean allPrerequisitesMet(List<ConnectedActivity> history, ConnectedActivity action) {
        return Arrays.stream(action.prerequisite).allMatch(pca -> history.stream().anyMatch(hca -> pca.id.equals(hca.id)));
    }

    /**
     * Ensures that an action can be taken when none all its exclude conditions have taken place
     *
     * @param history the history of the visited activities in the current playthrough
     * @param action  the action to be checked
     * @return whether this action can still be undertaken
     */
    private boolean allExcludesMet(List<ConnectedActivity> history, ConnectedActivity action) {
        return Arrays.stream(action.excludedBy).noneMatch(pca -> history.stream().anyMatch(hca -> pca.id.equals(hca.id)));
    }

    private List<AvailableAction> getAvailable(ConnectedActivity current, List<ConnectedActivity> history) {
        logger.info("current activities: " + current);
        return Arrays.stream(current.activities)
                .map(ca -> {
                    boolean prereq = allPrerequisitesMet(history, ca);
                    boolean excludes = allExcludesMet(history, ca);
                    logger.info("prereq: " + prereq + "; excludes: " + excludes + ";ca: " + ca + ";\nhistory: " + history + ";\n");
                    AvailableAction aa = new AvailableAction(prereq && excludes, ca);
                    return aa;
                })
                .collect(Collectors.toList());
    }

    /**
     * This method validates an action which was chosen.
     *
     * @param mapped
     * @param chosenAction
     */
    public void validateChosenOption(List<ConnectedActivity> mapped, ConnectedActivity chosenAction) {
        ConnectedActivity current = mapped.get(mapped.size() - 1);

        List<AvailableAction> previouslyAvailable = getAvailable(current, mapped);
        List<AvailableAction> matching = previouslyAvailable.stream().filter(aa -> aa.getBacking().id.equals(chosenAction.id)).collect(Collectors.toList());

        if (matching.size() > 1)
            // this should not happen, since RoomAndActivityComposerBean handles such cases
            throw new RuntimeException("there are multiple action with the same id");

        if (matching.size() == 0)
            throw new RuntimeException("The requested action " + chosenAction.id + " is not one of the previouslyAvailable actions in the current state " + current);

        AvailableAction chosenaa = matching.get(0);

        if (!chosenaa.getAvailable())
            throw new RuntimeException("chose an option which is previouslyAvailable");
    }

    /**
     * This method creates a {@link SSMGameState} in accordance to the history associated with a session id.
     *
     * @param sessionID the session id for which the {@link SSMGameState} should be found
     * @returns the current {@link SSMGameState} for this session id
     */
    public SSMGameState getCurrentState(String sessionID) {
        List<Integer> currentState = sessionServerice.getGameState(sessionID);
        List<ConnectedActivity> mapped = mapHistoryIntegerToCA(currentState);
        return getCurrentState(mapped);
    }

    /**
     * This method produces a game state from a {@link List} of {@link ConnectedActivity}s
     *
     * @param state the {@link List} of {@link ConnectedActivity}s, in their selection order (latest selection last)
     * @returns the derived game state
     */
    public SSMGameState getCurrentState(List<ConnectedActivity> state) {
        ConnectedActivity current = state.get(state.size() - 1);
        ConnectedActivity prev = null;

        if (state.size() > 2)
            prev = state.get(state.size() - 1);

        ConnectedActivity lastTopLevel = null;
        for (int i = state.size() - 1; i >= 0; i--) {
            ConnectedActivity ca = state.get(i);
            if (aab.isTopLevel(ca)) {
                lastTopLevel = ca;
                break;
            }
        }

        List<AvailableAction> available = getAvailable(current, state);
        return new SSMGameState(
                lastTopLevel != null ? lastTopLevel.id : null,
                current.description,
                current.result,
                available);
    }

    /**
     * This method handles the selection of an action. This is done by validating the choice and updating the {@link SSMGameState}.
     *
     * @param sessionID the session in which the choice was made
     * @param chosen    the id representation of the {@link ConnectedActivity} which was chosen
     * @returns the updated {@link SSMGameState}
     */
    public SSMGameState applyOption(String sessionID, CSMChoseOption chosen) {
        List<Integer> currentState = sessionServerice.getGameState(sessionID);

        List<ConnectedActivity> mapped = mapHistoryIntegerToCA(currentState);

        Integer chosenOption = chosen.getOption();
        ConnectedActivity chosenAction = aab.getSpecific(chosenOption).orElseThrow(() -> new RuntimeException("the chosen option " + chosenOption + " does not belong to any action"));

        validateChosenOption(mapped, chosenAction);
        mapped.add(chosenAction);

        String junpedResults = "";

        // if the chosen activity has only on sub activity it is an jump, which should be done recursively
        ConnectedActivity currentMapped = chosenAction;
        while (currentMapped.activities.length == 1) {
            ConnectedActivity onlyAct = currentMapped.activities[0];
            if (currentMapped.result != null) {
                String pre = junpedResults.length() > 0 ? "\n" : "";
                junpedResults += pre + currentMapped.result;
            }

            mapped.add(currentMapped = onlyAct);
        }

        List<Integer> newState = mapped.stream().map(v -> v.id).collect(Collectors.toList());
        sessionServerice.setGameState(sessionID, newState);

        SSMGameState newGameStateObjekt = getCurrentState(mapped);

        if (junpedResults.length() > 0)
            newGameStateObjekt.setDescription(junpedResults);

        return newGameStateObjekt;
    }
}
