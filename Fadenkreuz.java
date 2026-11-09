import sas.*;
import java.awt.Color;

public class Fadenkreuz
{
    Sprite kreuz;
    Circle treffpunkt;
    int tempo;

    // pX, pY = Mitte des Fadenkreuzes
    Fadenkreuz(double pX, double pY)
    {
        tempo = 8;

        kreuz = new Sprite(ring(pX, pY, 32, 28));
        kreuz.add(ring(pX, pY, 14, 11));
        kreuz.add(new Rectangle(pX - 46, pY - 1, 92, 3, Color.black));
        kreuz.add(new Rectangle(pX - 1, pY - 46, 3, 92, Color.black));

        // Kleiner Punkt in der Mitte, damit wird der Treffer mit intersects geprueft
        treffpunkt = new Circle(pX - 2, pY - 2, 2, Color.red);
    }

    // Ring aus Aussen- und Innenkreis, der innere Kreis laeuft andersherum und wird so zum Loch
    Polygon ring(double pX, double pY, double pAussen, double pInnen)
    {
        Polygon p = new Polygon(pX + pAussen, pY, Color.black);
        for (int i = 1; i <= 36; i++)
        {
            double w = Tools.degreeToRadian(i * 10);
            p.add(pAussen * Math.cos(w) - pAussen, pAussen * Math.sin(w));
        }
        for (int i = 0; i <= 36; i++)
        {
            double w = Tools.degreeToRadian(-i * 10);
            p.add(pInnen * Math.cos(w) - pAussen, pInnen * Math.sin(w));
        }
        return p;
    }

    void bewege(View pFenster)
    {
        double dx = 0;
        double dy = 0;

        if (pFenster.keyLeftPressed() && gibX() > 0)
            dx = -tempo;
        if (pFenster.keyRightPressed() && gibX() < 1600)
            dx = tempo;
        if (pFenster.keyUpPressed() && gibY() > 0)
            dy = -tempo;
        if (pFenster.keyDownPressed() && gibY() < 800)
            dy = tempo;

        kreuz.move(dx, dy);
        treffpunkt.move(dx, dy);
    }

    double gibX()
    {
        return treffpunkt.getCenterX();
    }

    double gibY()
    {
        return treffpunkt.getCenterY();
    }

    Circle gibTreffpunkt()
    {
        return treffpunkt;
    }
}
