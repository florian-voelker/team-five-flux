package de.teamFive.common.rooms;

import de.teamFive.common.activities.AbstractActivity;
import de.teamFive.common.activities.ActivityWithJump;
import de.teamFive.common.activities.ActivityWithSubActivities;

public class EntranceAirLock implements Room {


    @Override
    public AbstractActivity getRoom() {
        return new ActivityWithSubActivities(
                1,
                "Eingangsschleuse\n\nDu befindest dich in der Eingangschleuse der Flux. Vor dir befindet sich ein Tür, die zum Rest der Raumstation führt. Zu deiner Rechten siehst du ein Terminal, auf dem eine Signalleucht blinkt.",
                new AbstractActivity[]{
                        new ActivityWithSubActivities(
                                11,
                                "Terminal anschauen",
                                "Du trittst an das Terminal heran.\nHallo, ich bin die KI DeepSpace. Der Reaktor der Flux ist instabil geworden, wodurch die Stromversorgung des Schiffes schwankt. Die Crew hat zu dem Zeitpunkt des Fehlers geschlafen und ist momentan in ihren Schlafräumen gefangen.\nDie Tür vor dir ist auch vom Ausfall betroffen und kann momentan nicht automatisch geöffnet werden. Bitte öffne die Tür manuell mit Hilfe der unteren Aktionen.",
                                null,
                                null,
                                new AbstractActivity[]{
                                        new ActivityWithJump(
                                                21,
                                                "Tür öffnen",
                                                "*WUSSSSCH* Die Tür gleitet nach oben weg und gibt den Weg in den Eingangsbereich der Flux frei.",
                                                new Integer[]{21},
                                                null,
                                                11
                                        ),
                                        new ActivityWithJump(
                                                31,
                                                "Terminal verlassen",
                                                null,
                                                null,
                                                null,
                                                1
                                        )
                                }
                        ),
                        new ActivityWithJump(
                                41,
                                "Durch Tür treten",
                                null,
                                null,
                                new Integer[]{21},
                                2
                        )
                }
        );
    }
}