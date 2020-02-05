package de.teamFive.common.activities;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConnectedActivity {
    public Integer id;

    public String description;

    public String result;

    public ConnectedActivity[] excludedBy;
    public ConnectedActivity[] prerequisite;

    public ConnectedActivity[] activities;

    public ConnectedActivity(Integer id, String description, String result) {
        this.id = id;
        this.description = description;
        this.result = result;
    }

    public ConnectedActivity(Integer id, String description, String result, ConnectedActivity[] excludedBy, ConnectedActivity[] prerequisite, ConnectedActivity[] activities) {
        this.id = id;
        this.description = description;
        this.result = result;
        this.excludedBy = excludedBy;
        this.prerequisite = prerequisite;
        this.activities = activities;
    }

    @Override
    public String toString() {
        Function<ConnectedActivity[], String> map = arr -> Arrays.stream(arr).map(v -> v.id).collect(Collectors.toList()).toString();

        return "ConnectedActivity{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", result='" + result + '\'' +
                ", excludedBy=" + map.apply(excludedBy) +
                ", prerequisite=" + map.apply(prerequisite) +
                ", activities=" + map.apply(activities) +
                '}';
    }
}
