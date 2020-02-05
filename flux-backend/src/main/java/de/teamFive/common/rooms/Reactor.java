package de.teamFive.common.rooms;

import de.teamFive.common.activities.AbstractActivity;
import de.teamFive.common.activities.ActivityWithJump;
import de.teamFive.common.activities.ActivityWithSubActivities;

public class Reactor implements Room {

    @Override
    public AbstractActivity getRoom() {
        return new ActivityWithSubActivities(
            5,
            "Reaktor\n\nDu hast den Reaktorraum betreten",
            new AbstractActivity[]{
                    new ActivityWithSubActivities(
                            15,
                            "Den Reaktor betrachten",
                            "Die Kontrolleuchten zeigen dir, dass der Reaktor kurz vor einer Schmelze steht. Du musst so schnell wie möglich die Reparatur vornehmen wenn nicht das ganze Raumschiff in die Luft fliegen soll.",
                            null,
                            null,
                            new AbstractActivity[]{
                                    new ActivityWithJump(
                                            115,
                                            "Reaktor herunterfahren (Lebenserhaltungssysteme werden ebenfalls ausgeschaltet)",
                                            "Schweren Herzens betätigst du den Notausschalter des Reaktors, wohl wissend, dass viele der Crewmitglieder den Ausfall der Lebenserhaltungssysteme nicht überleben werden. Der Reaktor, jetzt deaktiviert, kühlt rapide ab.",
                                            new Integer[]{115},
                                            null,
                                            15
                                    ),
                                    new ActivityWithSubActivities(
                                            215,
                                            "Den höllenheißen Reaktor betreten und den Schaden reparieren",
                                            "Unter Todesqualen gelingt es dir, den Reaktor zu reparieren. Du wirst als Held in die Geschichte der Menschheit eingehen, jedoch wirst du das nie selbst erleben. Die Hitze im Reaktor hat dich buchstäblich gekocht und es dauert zu lange, bis Hilfe eingetroffen ist.\n\nDAS ENDE",
                                            null,
                                            null,
                                            new AbstractActivity[]{}
                                    ),
                                    new ActivityWithSubActivities(
                                            315,
                                            "Vom Reaktor wegtreten und weiter das Raumschiff erkunden",
                                            "Als du beschließt das Raumschiff weiter zu erkunden wird dir schnell klar, dass du die Kontrollleuchten wohl ernster hättest nehmen sollen. Der Reaktor überhitzt, und sprengt kurz darauf ein riesiges Loch in das Raumschiff. Du und alle Mitglieder der Mannschaft finden den Tod.\n\nDAS ENDE",
                                            null,
                                            null,
                                            new AbstractActivity[]{}
                                    ),
                            }
                    ),
                    new ActivityWithJump(
                            25,
                            "Laufe in den Kontrollraum",
                            null,
                            null,
                            null,
                            4
                    )
            }
        );
    }
}
