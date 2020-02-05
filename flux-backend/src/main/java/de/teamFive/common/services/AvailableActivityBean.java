package de.teamFive.common.services;

import de.teamFive.common.activities.AbstractActivity;
import de.teamFive.common.activities.ConnectedActivity;
import de.teamFive.common.rooms.*;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


/**
 * This bean's responsibility is to hold all the game's available Activities in form of Connected Activities. It
 * differentiates between the top-most level activities and all activities.
 */
@Singleton
@Startup
public class AvailableActivityBean {

    /**
     * This collection specifies the rooms (and thus the available activities) which shall be registered
     */
    private static final Collection<Class<? extends Room>> loadClasses = Arrays.asList(
            EntranceAirLock.class, EntranceArea.class, SecurityAirlock.class, ControlRoom.class, Reactor.class
    );
    @EJB
    RoomAndActivityComposerBean roomAndActivityComposerBean;
    private Collection<ConnectedActivity> entryActivities;
    private Map<Integer, ConnectedActivity> allActivities;

    private Logger logger = Logger.getLogger(AvailableActivityBean.class.getCanonicalName());

    /**
     * Extracts and collects the top-level activities from a collection of rooms
     *
     * @param clazzes a collection of rooms
     * @return The extracted and collected activities
     */
    private static Collection<AbstractActivity> getAllFromRooms(Collection<Class<? extends Room>> clazzes) {
        Method getRoom;

        try {
            getRoom = Room.class.getMethod("getRoom");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("could not get the getRoom method of the Room interface");
        }

        return clazzes.stream().map(c -> {
            try {
                return (AbstractActivity) getRoom.invoke(c.getConstructor().newInstance());
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("class " + c.getName() + " has no default constructor", e);
            }
        }).collect(Collectors.toList());
    }

    /**
     * @returns all top level activites
     */
    public Set<ConnectedActivity> getAll() {
        return new HashSet<>(entryActivities);
    }

    /**
     * Getter-method for one specific connected activity
     *
     * @param id the id associated with the requested activity
     * @return an optional which may hold the requested activity
     */
    public Optional<ConnectedActivity> getSpecific(int id) {
        if (allActivities.containsKey(id))
            return Optional.of(allActivities.get(id));
        else
            return Optional.empty();
    }

    /**
     * This method recursively registers an activity and all its associated activities. This may be activites specified by
     * {@link ConnectedActivity#excludedBy}, {@link ConnectedActivity#prerequisite} or {@link ConnectedActivity#activities}.
     *
     * @param ca
     */
    private void registerAll(ConnectedActivity ca) {
        // this check prevents duplicate activities from being registered
        if (allActivities.containsKey(ca.id))
            if (allActivities.get(ca.id).equals(ca))
                return;
            else
                throw new RuntimeException("allActivities list another activity with the id " + ca.id);

        allActivities.put(ca.id, ca);

        for (ConnectedActivity a : ca.activities)
            registerAll(a);

        for (ConnectedActivity a : ca.excludedBy)
            registerAll(a);

        for (ConnectedActivity a : ca.prerequisite)
            registerAll(a);
    }

    public boolean isTopLevel(ConnectedActivity ca) {
        return ca.id >= 1 && ca.id <= 5;
    }

    @PostConstruct
    public void init() {
        entryActivities = roomAndActivityComposerBean.createLinkedActivities(getAllFromRooms(loadClasses));

        allActivities = new HashMap<>();
        for (ConnectedActivity a : entryActivities)
            registerAll(a);

        logger.log(Level.INFO, "Loaded rooms: {0}", entryActivities);
    }
}
