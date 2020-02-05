package de.teamFive.common.rooms;

import de.teamFive.common.activities.AbstractActivity;
import de.teamFive.common.activities.ActivityWithJump;
import de.teamFive.common.activities.ActivityWithSubActivities;

public class SecurityAirlock implements Room {
    @Override
    public AbstractActivity getRoom() {
        return new ActivityWithSubActivities(
                3,
                "Sicherheitsschleuse",
                new AbstractActivity[]{
                        new ActivityWithSubActivities(
                                13,
                                "PDA untersuchen",
                                "Es scheint sich um das PDA eines Mitarbeiters zu handeln. Das Hintergrundbild ist ein junges Mädchen, welches mit einem zahnlückenreichen Grinsen in die Kamera lacht. Auf dem PDA sind ein Mail-Programm und eine Tagebuch-App geöffnet. Außerdem fühlst du seltsame Einkerbungen auf der Rückseite des PDAs, als du es anhebst",
                                null,
                                null,
                                new AbstractActivity[]{
                                        new ActivityWithSubActivities(
                                                113,
                                                "Emails lesen",
                                                "Das Postfach benötigt ein Passwort.",
                                                null,
                                                null,
                                                new AbstractActivity[]{
                                                        new ActivityWithJump(
                                                                5000,
                                                                "zurück zum PDA Menü",
                                                                null,
                                                                null,
                                                                null,
                                                                13),
                                                        new ActivityWithSubActivities(
                                                                1113,
                                                                StoryConstants.PDA_EMAIL_PASSWORD + " als Passwort eingeben",
                                                                "Hallo " + StoryConstants.PDA_OWNER_FIRST_NAME + ",\n\ndu hast drei neue Mails.",
                                                                null,
                                                                new Integer[]{213},
                                                                new AbstractActivity[]{
                                                                        new ActivityWithJump(
                                                                                11113,
                                                                                "Marsianerinnen in deiner Nähe wollen dich kennen lernen",
                                                                                "\u0462\u7429\udbfa\udc1b\u1b18\udaa6\udeff\udbe0\udcf0\ud8d4\udffe\ud91a\udfc392\u06b1\ud89d\uddad\u8358\udaee\udfc0\uda8d\ude41\u325e\ud982\ude1d\u0b79\ud9db\udc2d\u0556\u1a80u\u08ab\u0182\ue15f\u039f\u9ca7,{\ud9b0\udca4\u0275\u0150\u0360\ud8a4\udd91x\u07f7\u07fb\uce05\u6829\uda94\ude7f\u078e\ud9a4\ude84\ud9af\udd3b\u0325\u00e1\ud860\udf14\udae1\udf82\ua61e\ud8dc\udd49\u0524\udb70\udf0e\uda47\udc83\u85bc\ud96f\udc78\u0760\u7ca4\udb55\udf57\udb50\udd03\ud979\uddf7\u043d\uda77\uddfc\uef2c\ud8e5\udd61\uda4d\udc9c\u9f8f\u7533\ud994\ude3f\u0592\ud9fa\udc11|\u4d19u^\udb32\udfcf\u35b3n|W\ubc88\u016c$9\u06c2E\ua495\udad1\uded1\u047e\u45ae=\u0743\u0263\ua5ef\ud9fc\ude87\u722a\uf28f\u8796\u40de\u0442\ud921\udd06\u4c70\u07e2.ES!^\uda18\ude32jL\ufa8c\uf988\u034cl\u55eb\uf309\u8468\uda21\uddf1\ufffd\u032f\u14a4L\u0714\ufffd=\u0485x\udbdc\udf12\ud9bd\udeb7_\ud986\udd5b\u5071\u058b\udbfd\udf6d\udaed\udf6ab?\u960e\uc72eC\udb50\udf47~\u06bd]\u06dc\uda49\udeb9e\ud938\udce2\u0278\ud886\uddb2\u00ea\u052aC\udba4\udf15\ud48f\u71f7\u04da^\uda37\udc2e\u033f\u5178\ua527\u4c53$\u100f\u6868\u0558d\ud8d5\udd96\u1adf\ud81a\udee5\ud80d\udc920\uf036\udb27\udc2d\u90ac\u7e9b\ud1c5\udb74\udfde\u3403\ud2db\u02af\uda06\udeef\u7c17\udb2e\udc85\u7f1a\u090a\ufc18\u0313\ud9ba\udc5f\ud87e\udc19\ud973\udf98\ud87c\udfd0\uf0b2\ufffdx\ud3bc\u7f14\u0213a\ud894\ude4d\u4e1d\uf127$\udac2\udf06\u0343<\u14a5\u03fa\u00f4\udb4e\udf45\ua9e8\u7805\ud9b6\udef2\u3fce\u1f63XI}\ud849\udc54\ue71a!\ue3b2\u3632\u64d0\uda9f\udd29\u0230\uaf73\u0366\\u0520\u1365\u1fcb\u0532\u44a6\u02da\u96af\ud835\udf5cO\u0406\uf822\uaf4fC\u028bH\udbbd\udfae\ufdfb=\u0f68\uda00\ude74\ud8bf\udfd4\udb2d\udf86\udb76\ude15K\ud8c2\udea9lch\ud9ab\udcdf\u1572\uda55\udf77\ud9ea\uddd6`\u0666P\u02b1!\u0629\ud90c\udeed\u00dd\udb1e\udde9\ud8e0\ude45\u07d8A\u5245\ubce6\uf8da\u06271\ufb1d\ucc7f\u040fO\u0514\u078a\u52ear\udb14\udcb9\ua584\ub385-s\u21c2\uabe2ip\u0593\u0345U\ud9cd\udf78\u060f\udbab\udf0b7\u06d0\udbac\udf9cn\ub381\u5d52\ud905\uddbc\u05f3\ua9e4\uda52\udcd1U\ud89b\udd9a\u06f4\u9e6cI\u06f7N\ud946\ude05\u3676",
                                                                                null,
                                                                                null,
                                                                                1113
                                                                        ),
                                                                        new ActivityWithJump(
                                                                                21113,
                                                                                "Rundmail: Änderung des Schleusenpassworts",
                                                                                "Ciao Crewmitglieder,\nDas wichtigste direkt zum Start: Das neue Passwort für die Sicherheitsschleuse lautet " + StoryConstants.SECURITY_AIRLOCK_PASSWORD + ".\nAußerdem wird Euch inzwischen aufgefallen sein, dass die Tür des westlichen Trakts klemmt. Wir bitten Euch, dass Ihr pfleglich mit dieser Tür umgeht. Von " + StoryConstants.MECHANIC_FIRST_NAME + "  sollen wir Euch die Warnung mitteilen, dass die Tür bei der nächsten kleinen Störung so kaputt gehen wird, dass aufwendige Reparaturen benötigt werden würden.\nAbschließend möchten wir noch herzlich " + StoryConstants.PDA_OWNER_FIRST_NAME + " zu seinem 50. Geburtstag gratulieren. In der Kantine werdet Ihr Kuchen finden.\n\nBis zum nächsten Mal,\nEuer Newsletter-Team",
                                                                                null,
                                                                                null,
                                                                                1113
                                                                        ),
                                                                        new ActivityWithJump(
                                                                                31113,
                                                                                "Alles Gute Papa",
                                                                                "Lieber Papa,\nich wünsche dir alles Gute zum Geburtstag! Ich hoffe, dass du auch auf der Raumstation einen schönen Geburtstag mit Unmengen an Kuchen feiern kannst. Mama und ich haben gemeinsam selbst einen Kuchen gebacken. Wir essen den Kuchen gerade und denken an dich. Ich habe dir im Anhang mein Geschenk mitgeschickt, ich hoffe, dir gefällt es. Ich wünsche mir, dass du bald wieder bei uns bist. Ich und Mama vermissen dich.\n\nLiebe Grüße,\n" + StoryConstants.PDA_OWNER_DAUGHTER_FIRST_NAME + " (und Mama)\nPS: Von mir natürlich auch alles Gute, Schatz",
                                                                                null,
                                                                                null,
                                                                                1113
                                                                        ),
                                                                        new ActivityWithJump(
                                                                                41113,
                                                                                "Postfach verlassen",
                                                                                "Du schließt das Mail-Programm.",
                                                                                null,
                                                                                null,
                                                                                13
                                                                        )
                                                                }
                                                        )
                                                }
                                        ),
                                        new ActivityWithJump(
                                                213,
                                                "Rückseite untersuchen",
                                                "Auf der Rückseite ist die Zahlenfolge " + StoryConstants.PDA_EMAIL_PASSWORD + " eingeritzt.",
                                                null,
                                                null,
                                                13
                                        ),
                                        new ActivityWithJump(
                                                313,
                                                "PDA wieder ablegen",
                                                "Du legst das PDA wieder auf den Tisch zurück.",
                                                null,
                                                null,
                                                3
                                        )

                                }
                        ),
                        new ActivityWithJump(
                                23,
                                "Den Kontrollraum betreten",
                                null,
                                null,
                                new Integer[]{21113},
                                4
                        ),
                        new ActivityWithJump(
                                33,
                                "In den Eingangsbereich zurückkehren",
                                null,
                                null,
                                null,
                                2
                        )

                }
        );
    }
}
