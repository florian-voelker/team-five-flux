package de.teamFive.common.rooms;

import de.teamFive.common.activities.AbstractActivity;
import de.teamFive.common.activities.ActivityWithJump;
import de.teamFive.common.activities.ActivityWithSubActivities;

public class ControlRoom implements Room {
    @Override
    public AbstractActivity getRoom() {
        return new ActivityWithSubActivities(
                4,
                "Kontrollraum\n\nVor dir liegt der Kontrollraum des Raumschiffes. An der linken Wand kannst du mehrere Terminals erkennen. In der Mitte des Raumes steht ein Holotisch, auf dem das Raumschiff zu sehen ist mit den verschiedenen Stati der Systeme.",
                new AbstractActivity[]{
                        new ActivityWithJump(
                                240,
                                "Rechtest Terminal betrachten",
                                "Du kannst sehen, dass bis auf den Reaktor das Raumschiff in einem guten Zustand zu sein scheint. Der Reaktor scheint sich aber immer weiter aufzuheitzen.",
                                null,
                                null,
                                4
                        ),
                        new ActivityWithJump(
                                241,
                                "In den Reaktorraum treten",
                                null,
                                null,
                                null,
                                5
                        ),
                        new ActivityWithJump(
                                24,
                                "Die Sicherheitsschleuse betreten",
                                null,
                                null,
                                null,
                                3
                        ),
                }
        );
    }
}
