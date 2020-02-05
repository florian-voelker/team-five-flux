package de.teamFive.common.entities;

import de.teamFive.common.activities.ConnectedActivity;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AvailableAction {
    private Boolean available;
    private transient ConnectedActivity backing;

    public String getDescription() {
        return backing.description;
    }

    public Integer getId() {
        return backing.id;
    }
}