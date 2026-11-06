import sas.*;
import java.awt.Color;

public class Spiel
{
    View fenster;
    Welt welt;
    Huhn[] huehner;
    Munition munition;
    Fadenkreuz fadenkreuz;
    Text punkteAnzeige, trefferText;

    int punkte;
    int trefferZaehler;
    boolean leertasteVorher;

    public static void main(String[] args)
    {
        new Spiel();
    }

    Spiel()
    {
        fenster = new View(1600, 800, "TrueMoorhuhn");
        fenster.setBackgroundColor(new Color(105, 200, 230));

        welt = new Welt();

        // Erst die fernen, dann die nahen Huehner erzeugen, damit die nahen vorne liegen
        Color[] farben = { new Color(170, 95, 40), new Color(240, 240, 235), new Color(75, 70, 70) };
        huehner = new Huhn[12];
        for (int i = 0; i < huehner.length; i++)
        {
            int entfernung = 3 - i / 4;
            int richtung = (i % 2 == 0) ? 1 : -1;
            Color farbe = farben[Tools.randomNumber(0, 2)];
            huehner[i] = new Huhn(entfernung, richtung, farbe, 1600, 800);
        }

        punkte = 0;
        punkteAnzeige = new Text(20, 20, "Punkte: 0", Color.black);
        punkteAnzeige.setFontMonospaced(true, 28);

        trefferText = new Text(0, 0, "", Color.red);
        trefferText.setFontSansSerif(true, 30);
        trefferText.setHidden(true);
        trefferZaehler = 0;

        // 8 Patronen unten rechts
        munition = new Munition(1396, 732);

        fadenkreuz = new Fadenkreuz(800, 400, 1600, 800);

        leertasteVorher = false;

        while (true)
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

            for (int i = 0; i < huehner.length; i++)
            {
                huehner[i].bewege();
                if (huehner[i].istRaus())
                    huehner[i].neuStarten();
            }

            // "+Punkte" steigt kurz nach oben und verschwindet
            if (trefferZaehler > 0)
            {
                trefferZaehler--;
                trefferText.move(0, -1);
                if (trefferZaehler == 0)
                    trefferText.setHidden(true);
            }

            fenster.wait(16);
        }
    }

    // Von hinten durchs Array, damit das vorderste (naechste) Huhn getroffen wird
    void schiessen()
    {
        if (!munition.schiessen())
            return;

        for (int i = huehner.length - 1; i >= 0; i--)
        {
            if (huehner[i].trifft(fadenkreuz.gibTreffpunkt()))
            {
                huehner[i].abschiessen();
                punkte = punkte + huehner[i].gibPunkte();
                punkteAnzeige.setText("Punkte: " + punkte);

                trefferText.setText("+" + huehner[i].gibPunkte());
                trefferText.moveTo(fadenkreuz.gibX() + 20, fadenkreuz.gibY() - 50);
                trefferText.setHidden(false);
                trefferZaehler = 40;
                return;
            }
        }
    }
}
