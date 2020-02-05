package de.teamFive.common.activities;

public abstract class AbstractActivity {
    public final Integer id;
    public final String description;

    public final String result;

    public final Integer[] excludedBy;
    public final Integer[] prerequisite;

    public AbstractActivity(Integer id, String description) {
        this(id, description, null, null, null);
    }

    public AbstractActivity(Integer id, String description, String result, Integer[] excludedBy, Integer[] prerequisite) {
        if (id == null)
            throw new RuntimeException("id needs to be not null");

        /*if (description == null)
            throw new RuntimeException("description needs to be not null");*/

        if (excludedBy == null)
            excludedBy = new Integer[0];

        if (prerequisite == null)
            prerequisite = new Integer[0];

        this.id = id;
        this.description = description;
        this.result = result;
        this.excludedBy = excludedBy;
        this.prerequisite = prerequisite;
    }
}
