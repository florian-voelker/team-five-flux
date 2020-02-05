package de.teamFive.common.rooms;


import de.teamFive.common.activities.AbstractActivity;
import de.teamFive.common.activities.ActivityWithJump;
import de.teamFive.common.activities.ActivityWithSubActivities;

public class EntranceArea implements Room {

    @Override
    public AbstractActivity getRoom() {
        return new ActivityWithSubActivities(
                2,
                "Eingangsbereich\n\nDu stehst im Eingangsbereich des Raumschiffes. Oben liegen die Dockstationen der Shuttles und die dazugehörigen Schleusen. Rechts siehst du die Spinde der Mannschaft des Schiffs",
                new AbstractActivity[]{
                        new ActivityWithSubActivities(
                                12,
                                "Spinde anschauen",
                                "Als du dich den Spinden näherst siehst du, dass ein Spind nur angelehnt ist. Das Schloss eines anderen Spindes sieht so verrostet aus, dass du den Spind wahrscheinlich mit Gewalt aufmachen.",
                                null,
                                null,
                                new AbstractActivity[]{
                                        new ActivityWithJump(
                                                112,
                                                "Verrosteten Spind mit Gewalt öffnen",
                                                "Mit einem kräftigen Ruck schaffst du es tatsächlich, die Tür des Spindes aufzustemmen. Anscheinend hast du aber etwas zu viel Kraft verwendet, denn aus der oberen Ablage fällt dir " + StoryConstants.RUSTED_LOCKER_ITEM + " entgegen. Bevor du reagieren und es auffangen kannst zerschellt es mit einem hellen Klirren auf dem Boden in irreparable Einzelteile.",
                                                new Integer[]{112},
                                                null,
                                                12
                                        ),
                                        new ActivityWithJump(
                                                212,
                                                "In den verrosteten Spind hineinschauen",
                                                "Du siehst einige Familienfotos, ein Paar Handschuhe sowie ein selbstgemaltes Bild, was vermutlich von einen Kind gemalt worden ist und ein Mann und ein Kind zeigt.",
                                                null,
                                                new Integer[]{112},
                                                12
                                        ),
                                        new ActivityWithJump(
                                                312,
                                                "Den Eingangsbereich wieder betrachten",
                                                null,
                                                null,
                                                null,
                                                2
                                        )
                                }
                        ),
                        new ActivityWithJump(
                                242,
                                "Zur Eingangsschleuse gehen",
                                null,
                                null,
                                null,
                                1
                        ),
                        new ActivityWithJump(
                                243,
                                "Sicherheitsschleuse betreten",
                                null,
                                null,
                                null,
                                3
                        )

                }
        );
    }
}