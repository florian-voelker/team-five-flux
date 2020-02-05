package de.teamFive.common.activities;

public class ActivityWithJump extends AbstractActivity {

    public final Integer jump;

    public ActivityWithJump(Integer id, String description, String result, Integer[] excludedBy, Integer[] prerequisite, Integer jump) {
        super(id, description, result, excludedBy, prerequisite);

        if (jump == null)
            throw new RuntimeException("Jump needs to be defined");

        this.jump = jump;
    }
}
