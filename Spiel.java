import sas.*;
import java.awt.Color;

public class Spiel
{
    View fenster;
    Welt welt;
    Huhn[] huehner;
    Anzeige anzeige;
    Munition munition;
    Fadenkreuz fadenkreuz;
    Rectangle tafel;
    Text titel, zeile1, zeile2, pauseText;

    int punkte, schuesse, highscore, spielzeit;
    long startZeit;
    boolean leertasteVorher;

    public static void main(String[] args)
    {
        new Spiel();
    }

    Spiel()
    {
        fenster = new View(1600, 800, "TrueMoorhuhn");
        welt = new Welt();

        // Erst die fernen, dann die nahen Huehner erzeugen, damit die nahen vorne liegen
        Color[] farben = { new Color(170, 95, 40), new Color(240, 240, 235), new Color(75, 70, 70) };
        huehner = new Huhn[12];
        for (int i = 0; i < huehner.length; i++)
        {
            int entfernung = 3 - i / 4;
            int richtung = (i % 2 == 0) ? 1 : -1;
            huehner[i] = new Huhn(entfernung, richtung, farben[Tools.randomNumber(0, 2)]);
        }

        anzeige = new Anzeige(0, 752);
        munition = new Munition(1396, 732);

        // Tafel fuer Start und Spielende
        tafel = new Rectangle(300, 200, 1000, 320, new Color(255, 250, 230));
        titel = new Text(340, 230, "", Color.red);
        titel.setFontSansSerif(true, 60);
        zeile1 = new Text(340, 340, "", Color.black);
        zeile1.setFontSansSerif(false, 26);
        zeile2 = new Text(340, 430, "", new Color(0, 120, 0));
        zeile2.setFontSansSerif(true, 34);

        pauseText = new Text(560, 330, "Pause - P = weiter", Color.red);
        pauseText.setFontSansSerif(true, 50);
        pauseText.setHidden(true);

        fadenkreuz = new Fadenkreuz(800, 400);

        spielzeit = 90;
        highscore = 0;
        anzeige.aktualisiere(0, 0, spielzeit);

        tafelZeigen("TrueMoorhuhn",
                    "Pfeiltasten = Zielen,  Leertaste = Schießen,  R = Nachladen,  P = Pause",
                    "Enter = Start");
        while (true)
        {
            spielen();

            String rekord = "Highscore: " + highscore;
            if (punkte > highscore)
            {
                highscore = punkte;
                rekord = "Neuer Highscore!";
            }
            tafelZeigen("Zeit um!  " + punkte + " Punkte",
                        rekord + "   (" + schuesse + " Schüsse)",
                        "Enter = Neustart");
        }
    }

    void spielen()
    {
        punkte = 0;
        schuesse = 0;
        leertasteVorher = false;
        munition.auffuellen();
        for (int i = 0; i < huehner.length; i++)
            huehner[i].neuStarten();

        startZeit = Tools.getStartTime();
        int restZeit = spielzeit;

        while (restZeit > 0)
        {
            fadenkreuz.bewege(fenster);

            // Nur beim Druecken schiessen, nicht solange die Taste gehalten wird
            boolean leertaste = fenster.keyPressed(' ');
            if (leertaste && !leertasteVorher)
                schiessen();
            leertasteVorher = leertaste;

            if (fenster.keyPressed('r'))
                munition.nachladen();
            munition.aktualisiere();

            if (fenster.keyPressed('p'))
                pause();

            // Schwierigkeit: die Huehner werden mit der Zeit schneller, am Ende 1,6-fach
            float vergangen = Tools.getElapsedTime(startZeit);
            for (int i = 0; i < huehner.length; i++)
            {
                huehner[i].bewege(1 + vergangen / 150);
                if (huehner[i].istRaus())
                    huehner[i].neuStarten();
            }

            restZeit = spielzeit - (int) vergangen;
            anzeige.aktualisiere(punkte, schuesse, restZeit);

            fenster.wait(16);
        }
    }

    // Von hinten durchs Array, damit das vorderste (naechste) Huhn getroffen wird
    void schiessen()
    {
        if (!munition.schiessen())
            return;
        schuesse++;

        for (int i = huehner.length - 1; i >= 0; i--)
        {
            if (huehner[i].trifft(fadenkreuz.gibTreffpunkt()))
            {
                huehner[i].abschiessen();
                punkte = punkte + huehner[i].gibPunkte();
                return;
            }
        }
    }

    // Wartet, bis P losgelassen, nochmal gedrueckt und wieder losgelassen wird.
    // Die Pausenzeit wird auf die Startzeit aufgeschlagen, damit sie nicht zaehlt.
    void pause()
    {
        long pauseStart = Tools.getStartTime();
        pauseText.setHidden(false);

        while (fenster.keyPressed('p'))
            fenster.wait(16);
        while (!fenster.keyPressed('p'))
            fenster.wait(16);
        while (fenster.keyPressed('p'))
            fenster.wait(16);

        pauseText.setHidden(true);
        startZeit = startZeit + (Tools.getStartTime() - pauseStart);
    }

    // Zeigt die Tafel und wartet auf Enter
    void tafelZeigen(String pTitel, String pZeile1, String pZeile2)
    {
        titel.setText(pTitel);
        zeile1.setText(pZeile1);
        zeile2.setText(pZeile2);
        tafelSichtbar(true);

        while (!fenster.keyEnterPressed())
            fenster.wait(16);

        tafelSichtbar(false);
    }

    void tafelSichtbar(boolean pSichtbar)
    {
        tafel.setHidden(!pSichtbar);
        titel.setHidden(!pSichtbar);
        zeile1.setHidden(!pSichtbar);
        zeile2.setHidden(!pSichtbar);
    }
}
