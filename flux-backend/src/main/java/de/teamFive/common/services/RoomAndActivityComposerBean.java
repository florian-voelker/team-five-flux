package de.teamFive.common.services;

import de.teamFive.common.activities.AbstractActivity;
import de.teamFive.common.activities.ActivityWithJump;
import de.teamFive.common.activities.ActivityWithSubActivities;
import de.teamFive.common.activities.ConnectedActivity;

import javax.ejb.Stateless;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * This bean takes in {@link AbstractActivity}s and converts them to {@link ConnectedActivity}s. In this process
 * the ids of other {@link AbstractActivity}s in the fields {@link AbstractActivity#prerequisite}, {@link AbstractActivity#excludedBy},
 * {@link ActivityWithSubActivities#activities} and {@link ActivityWithJump#jump} are replaced with references to concrete
 * {@link ConnectedActivity}s. Duplicate {@link ConnectedActivity}s are avoided.
 */
@Stateless
public class RoomAndActivityComposerBean {

    private Map<Integer, AbstractActivity> registeredActivities;
    private Map<Integer, ConnectedActivity> connectedActivities;

    private static <F, T> T[] mapArray(F[] from, Class<T> clazz, Function<F, T> mapper) {
        T[] res = (T[]) Array.newInstance(clazz, from.length);
        for (int i = 0; i < from.length; i++)
            res[i] = mapper.apply(from[i]);

        return res;
    }

    /**
     * This method is the entry point of this bean. For more information regarding this bean see {@link RoomAndActivityComposerBean}.
     *
     * @param roomDefinition the {@link AbstractActivity}s to be transformed into {@link ConnectedActivity}s
     */
    public Collection<ConnectedActivity> createLinkedActivities(Iterable<AbstractActivity> roomDefinition) {
        registeredActivities = new HashMap<>();
        connectedActivities = new HashMap<>();

        for (AbstractActivity aa : roomDefinition)
            registerActivity(aa);

        return linkAndValidateIntegrity();
    }

    /**
     * This method registers a {@link AbstractActivity} in {@link #registeredActivities} if it is not already present there
     * as well as its subactivities if it is a {@link ActivityWithSubActivities}.
     * @param aa
     */
    private void registerActivity(AbstractActivity aa) {
        AbstractActivity reg = registeredActivities.get(aa.id);

        if (reg != null && reg != aa)
            throw new RuntimeException("The key " + aa.id + " was already registered for activity " + reg.description + ", tried to register activity " + aa.description);

        if (aa instanceof ActivityWithSubActivities) {
            ActivityWithSubActivities aasa = (ActivityWithSubActivities) aa;
            for (AbstractActivity aasaAa : aasa.activities)
                registerActivity(aasaAa);
        }

        registeredActivities.put(aa.id, aa);
    }

    /**
     * This method initiates the lookup and potential creation of {@link ConnectedActivity}s which are the equivalent
     * of {@link AbstractActivity}s that the ids represent.
     * @param aa the corresponding activity, for which the resolve was called. only relevant for logging
     * @param ids the ids of {@link AbstractActivity}s which are to be resolved and thus potentially transformed to {@link ConnectedActivity}s
     * @returns the {@link ConnectedActivity} which correspond to the {@link AbstractActivity}s the ids represented
     */
    private ConnectedActivity[] resolve(AbstractActivity aa, Integer[] ids) {
        return mapArray(ids, ConnectedActivity.class, id -> {
            AbstractActivity resolved = registeredActivities.get(id);
            if (resolved == null)
                throw new RuntimeException("could not find the activity with the id " + id + " needed by activity " + aa.id + " in 'excludedBy'");

            return transformToConnectedActivity(resolved);
        });
    }

    /**
     * This method takes an {@link AbstractActivity} and converts it to a {@link ConnectedActivity} if this has not
     * already happened. Fields of the {@link AbstractActivity}, which formerly held an id referring to other
     * {@link AbstractActivity}s are now replaced with fields holding the actual equivalents of that referred object in form of
     * a {@link ConnectedActivity}.
     * @param aa is the {@link AbstractActivity} which is to be converted.
     * @returns the newly created and connected {@link ConnectedActivity}
     */
    private ConnectedActivity transformToConnectedActivity(AbstractActivity aa) {
        // looks up the id in the collection of existing connected activities to prevent doubles and endless loops
        ConnectedActivity alreadyConnected = connectedActivities.get(aa.id);

        if (alreadyConnected != null)
            return alreadyConnected;

        // creates a connected activity with the same basic properties of the abstract activity and adds it to the
        // collection of known connected activities
        ConnectedActivity instance = new ConnectedActivity(aa.id, aa.description, aa.result);

        connectedActivities.put(instance.id, instance);

        // Transforms the excludedBy and prerequisite from int[] (which held the ids) to ConnectedActivity[] (which holds the actual instances)
        instance.excludedBy = resolve(aa, aa.excludedBy);
        instance.prerequisite = resolve(aa, aa.prerequisite);

        /*
         * Similar transformation as one step before. This time the activities of the ConnectedActivity is filled
         * in one of two ways depending on what kind of activity the AbstractActivity is:
         * ActivityWithJump: The jump target is now the singular instance in the array (after transformation)
         * ActivityWithSubActivities: The subactivities are transformed to the activities
         */

        if (aa instanceof ActivityWithJump) {
            ActivityWithJump awj = (ActivityWithJump) aa;

            instance.activities = resolve(aa, new Integer[]{awj.jump});
        } else if (aa instanceof ActivityWithSubActivities) {
            ActivityWithSubActivities awsa = (ActivityWithSubActivities) aa;

            instance.activities = mapArray(awsa.activities, ConnectedActivity.class, this::transformToConnectedActivity);
        } else
            throw new RuntimeException("have not resolve strategy for " + aa.getClass().getName());

        return instance;
    }

    private Collection<ConnectedActivity> linkAndValidateIntegrity() {
        for (AbstractActivity aa : registeredActivities.values())
            transformToConnectedActivity(aa);

        return connectedActivities.values();
    }
}
