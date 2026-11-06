import sas.*;
import java.awt.Color;

public class Spiel
{
    View fenster;
    Welt welt;
    Huhn[] huehner;
    Fadenkreuz fadenkreuz;

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
            huehner[i] = new Huhn(entfernung, richtung, farbe, 1600);
        }

        fadenkreuz = new Fadenkreuz(800, 400, 1600, 800);

        while (true)
        {
            fadenkreuz.bewege(fenster);

            for (int i = 0; i < huehner.length; i++)
            {
                huehner[i].bewege();
                if (huehner[i].istRaus())
                    huehner[i].neuStarten();
            }

            fenster.wait(16);
        }
    }
}
