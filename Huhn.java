import sas.*;
import java.awt.Color;

public class Huhn
{
    Sprite huhn;
    Polygon fluegelOben, fluegelUnten;
    double x, y, groesse, faktor, tempo;
    int richtung, entfernung, punkte, breite;
    int flatterZaehler;
    double welleWinkel, welleHoehe, welleTempo;

    // Das Huhn wird auf einer 100x80 grossen Vorlage gezeichnet und je nach Entfernung skaliert.
    // pEntfernung: 1 = nah (gross, langsam), 2 = mittel, 3 = fern (klein, schnell)
    // pRichtung: 1 = fliegt nach rechts, -1 = fliegt nach links (dann wird gespiegelt)
    Huhn(int pEntfernung, int pRichtung, Color pFarbe, int pBreite)
    {
        entfernung = pEntfernung;
        richtung = pRichtung;
        breite = pBreite;
        x = 0;
        y = 0;
        flatterZaehler = 0;

        if (entfernung == 1)
        {
            groesse = 130;
            tempo = 3;
            punkte = 5;
        }
        else if (entfernung == 2)
        {
            groesse = 85;
            tempo = 4.5;
            punkte = 10;
        }
        else
        {
            groesse = 50;
            tempo = 6;
            punkte = 25;
        }
        faktor = groesse / 100;

        Color dunkel = pFarbe.darker();
        Color hell = new Color(245, 230, 200);
        Color rot = new Color(220, 30, 30);
        Color orange = new Color(250, 160, 20);

        // Schwanzfedern
        huhn = new Sprite(polygon(new double[] {22, 34, 2, 10, 10, 14, 6, 24, 24, 40}, dunkel));
        huhn.add(polygon(new double[] {22, 40, 0, 32, 8, 34, 2, 46, 22, 50}, Color.black));

        // Beine
        huhn.add(rechteck(38, 64, 4, 14, orange));
        huhn.add(rechteck(50, 64, 4, 14, orange));

        // Koerper und Bauch
        huhn.add(ellipse(14, 25, 62, 46, pFarbe));
        huhn.add(ellipse(32, 44, 40, 24, hell));

        // Kopf mit Kamm, Auge, Schnabel und Kehllappen
        huhn.add(kreis(67, 0, 5, rot));
        huhn.add(kreis(75, -4, 5, rot));
        huhn.add(kreis(83, 0, 5, rot));
        huhn.add(kreis(62, 6, 17, pFarbe));
        huhn.add(ellipse(88, 30, 8, 12, rot));
        huhn.add(polygon(new double[] {92, 18, 108, 24, 92, 30}, orange));
        huhn.add(kreis(78, 12, 6, Color.white));
        huhn.add(kreis(83, 15, 3, Color.black));

        // Fluegel, abwechselnd sichtbar
        fluegelOben = polygon(new double[] {28, 42, 64, 40, 60, 14, 54, 20, 50, -2, 44, 12, 38, -6, 34, 12, 24, 4}, dunkel);
        fluegelUnten = polygon(new double[] {28, 44, 64, 44, 60, 68, 54, 62, 50, 82, 44, 68, 38, 80, 34, 66, 24, 72}, dunkel);
        huhn.add(fluegelOben);
        huhn.add(fluegelUnten);
        fluegelUnten.setHidden(true);

        neuStarten();
    }

    // x-Wert auf der Vorlage, bei richtung -1 gespiegelt
    double sx(double pDx, double pBreite)
    {
        if (richtung == 1)
            return x + pDx * faktor;
        return x + (100 - pDx - pBreite) * faktor;
    }

    Ellipse ellipse(double pDx, double pDy, double pB, double pH, Color pFarbe)
    {
        return new Ellipse(sx(pDx, pB), y + pDy * faktor, pB * faktor, pH * faktor, pFarbe);
    }

    Circle kreis(double pDx, double pDy, double pR, Color pFarbe)
    {
        return new Circle(sx(pDx, 2 * pR), y + pDy * faktor, pR * faktor, pFarbe);
    }

    Rectangle rechteck(double pDx, double pDy, double pB, double pH, Color pFarbe)
    {
        return new Rectangle(sx(pDx, pB), y + pDy * faktor, pB * faktor, pH * faktor, pFarbe);
    }

    // pPunkte = {x1, y1, x2, y2, ...} auf der Vorlage
    Polygon polygon(double[] pPunkte, Color pFarbe)
    {
        Polygon p = new Polygon(sx(pPunkte[0], 0), y + pPunkte[1] * faktor, pFarbe);
        for (int i = 2; i < pPunkte.length; i += 2)
        {
            double dx = (pPunkte[i] - pPunkte[0]) * faktor * richtung;
            double dy = (pPunkte[i + 1] - pPunkte[1]) * faktor;
            p.add(dx, dy);
        }
        return p;
    }

    // Fliegt geradeaus in Flugrichtung und auf einer Wellenlinie hoch und runter
    void bewege()
    {
        double altY = Math.sin(welleWinkel) * welleHoehe;
        welleWinkel = welleWinkel + welleTempo;
        double neuY = Math.sin(welleWinkel) * welleHoehe;
        huhn.move(tempo * richtung, neuY - altY);

        flatterZaehler++;
        if (flatterZaehler >= 8)
        {
            flatterZaehler = 0;
            fluegelOben.setHidden(!fluegelOben.getHidden());
            fluegelUnten.setHidden(!fluegelUnten.getHidden());
        }
    }

    boolean istRaus()
    {
        if (richtung == 1)
            return huhn.getShapeX() > breite;
        return huhn.getShapeX() + huhn.getShapeWidth() < 0;
    }

    // Startet ausserhalb des Fensters mit zufaelligem Abstand, damit die Huehner nicht alle gleichzeitig kommen.
    // Ferne Huehner fliegen hoeher, nahe tiefer.
    void neuStarten()
    {
        double neuY;
        if (entfernung == 1)
            neuY = Tools.randomNumber(250, 480);
        else if (entfernung == 2)
            neuY = Tools.randomNumber(130, 420);
        else
            neuY = Tools.randomNumber(40, 330);

        int abstand = Tools.randomNumber(0, 1500);
        if (richtung == 1)
            huhn.moveTo(-huhn.getShapeWidth() - abstand, neuY);
        else
            huhn.moveTo(breite + abstand, neuY);

        welleWinkel = 0;
        welleHoehe = Tools.randomNumber(10, 60);
        welleTempo = Tools.randomNumber(2, 6) / 100.0;
    }

    int gibPunkte()
    {
        return punkte;
    }
}
