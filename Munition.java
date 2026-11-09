import sas.*;
import java.awt.Color;

public class Munition
{
    Rectangle[] huelsen, boeden;
    Text hinweis;
    int anzahl, maxAnzahl;
    boolean laedtNach;
    long nachladeStart;

    // pX, pY = linke obere Ecke der ersten Patrone
    Munition(double pX, double pY)
    {
        maxAnzahl = 8;
        anzahl = maxAnzahl;
        laedtNach = false;

        huelsen = new Rectangle[maxAnzahl];
        boeden = new Rectangle[maxAnzahl];
        for (int i = 0; i < maxAnzahl; i++)
        {
            huelsen[i] = new Rectangle(pX + i * 24, pY, 16, 36, new Color(200, 30, 30));
            boeden[i] = new Rectangle(pX + i * 24, pY + 36, 16, 12, new Color(215, 170, 50));
        }

        hinweis = new Text(pX, pY - 40, "R = Nachladen", Color.red);
        hinweis.setFontSansSerif(true, 24);
        hinweis.setHidden(true);
    }

    // Liefert true, wenn geschossen werden konnte. Die rechte Patrone verschwindet zuerst.
    boolean schiessen()
    {
        if (laedtNach || anzahl == 0)
            return false;

        anzahl--;
        huelsen[anzahl].setHidden(true);
        boeden[anzahl].setHidden(true);

        if (anzahl == 0)
        {
            hinweis.setText("R = Nachladen");
            hinweis.setHidden(false);
        }
        return true;
    }

    void nachladen()
    {
        if (laedtNach || anzahl == maxAnzahl)
            return;

        laedtNach = true;
        nachladeStart = Tools.getStartTime();
        hinweis.setText("Nachladen ...");
        hinweis.setHidden(false);
    }

    // Wird in jedem Frame aufgerufen und beendet das Nachladen nach 1 Sekunde
    void aktualisiere()
    {
        if (laedtNach && Tools.getElapsedTime(nachladeStart) >= 1.0)
            auffuellen();
    }

    void auffuellen()
    {
        laedtNach = false;
        anzahl = maxAnzahl;
        for (int i = 0; i < maxAnzahl; i++)
        {
            huelsen[i].setHidden(false);
            boeden[i].setHidden(false);
        }
        hinweis.setHidden(true);
    }
}
