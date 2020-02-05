package de.teamFive.common.activities;

import java.util.Arrays;

public class ActivityWithSubActivities extends AbstractActivity {
    public final AbstractActivity[] activities;

    public ActivityWithSubActivities(Integer id, String result, AbstractActivity[] activities) {
        this(id, null, result, null, null, activities);
    }

    public ActivityWithSubActivities(Integer id, String description, String result, Integer[] excludedBy, Integer[] prerequisite, AbstractActivity[] activities) {
        super(id, description, result, excludedBy, prerequisite);

        if (activities == null)
            activities = new AbstractActivity[0];

        this.activities = activities;

    }

    @Override
    public String toString() {
        return "ActivityWithSubActivities{" +
                "activities=" + Arrays.toString(activities) +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", result='" + result + '\'' +
                ", excludedBy=" + Arrays.toString(excludedBy) +
                ", prerequisite=" + Arrays.toString(prerequisite) +
                '}';
    }
}
