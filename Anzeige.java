import sas.*;
import java.awt.Color;

public class Anzeige
{
    Rectangle hintergrund;
    Text text;

    // Weisser Kasten mit Punkten, Schuessen und Restzeit, pX, pY = linke obere Ecke
    Anzeige(double pX, double pY)
    {
        hintergrund = new Rectangle(pX, pY, 560, 48, Color.white);
        text = new Text(pX + 12, pY + 8, "", Color.black);
        text.setFontSerif(true, 28);
    }

    void aktualisiere(int pPunkte, int pSchuesse, int pZeit)
    {
        text.setText("Punkte: " + pPunkte + ",  Schüsse: " + pSchuesse + ",  Zeit: " + pZeit);

        // Die letzten 10 Sekunden in Rot
        if (pZeit <= 10)
            text.setFontColor(Color.red);
        else
            text.setFontColor(Color.black);
    }
}
