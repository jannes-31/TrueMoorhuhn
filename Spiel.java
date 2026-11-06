import sas.*;
import java.awt.Color;

public class Spiel
{
    View fenster;
    Welt welt;
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
        fadenkreuz = new Fadenkreuz(800, 400);

        while (true)
        {
            fadenkreuz.bewege(fenster);
            fenster.wait(16);
        }
    }
}
