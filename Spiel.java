import sas.*;
import java.awt.Color;

public class Spiel
{
    View fenster;
    Welt welt;
    Huhn huhn;
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
        huhn = new Huhn(-120, 250, 120, 1, new Color(170, 95, 40));
        fadenkreuz = new Fadenkreuz(800, 400, 1600, 800);

        while (true)
        {
            fadenkreuz.bewege(fenster);

            huhn.bewege();
            if (huhn.istRaus(1600))
                huhn.neuStarten(1600, 800);

            fenster.wait(16);
        }
    }
}
