import sas.*;
import java.awt.Color;

public class Huhn
{
    Sprite huhn;
    Polygon fluegelOben, fluegelUnten;
    double tempo, fallTempo, welleWinkel, welleHoehe;
    int richtung, punkte, flugHoehe, flatterZaehler;
    boolean getroffen;

    // pEntfernung: 1 = nah (gross, langsam), 2 = mittel, 3 = fern (klein, schnell, hoch)
    // pRichtung: 1 = fliegt nach rechts, -1 = fliegt nach links (dann wird gespiegelt)
    Huhn(int pEntfernung, int pRichtung, Color pFarbe)
    {
        richtung = pRichtung;
        getroffen = false;
        flatterZaehler = 0;
        double faktor;

        if (pEntfernung == 1)
        {
            faktor = 1.3;
            tempo = 3;
            punkte = 5;
            flugHoehe = 250;
        }
        else if (pEntfernung == 2)
        {
            faktor = 0.85;
            tempo = 4.5;
            punkte = 10;
            flugHoehe = 140;
        }
        else
        {
            faktor = 0.5;
            tempo = 5;
            punkte = 25;
            flugHoehe = 40;
        }

        Color dunkel = pFarbe.darker();
        Color hell = new Color(245, 230, 200);
        Color rot = new Color(220, 30, 30);
        Color orange = new Color(250, 160, 20);

        // Das Huhn wird nach rechts schauend in Originalgroesse (ca. 100x80) gezeichnet

        // Schwanzfedern
        huhn = new Sprite(polygon(new double[] {22, 34, 2, 10, 10, 14, 6, 24, 24, 40}, dunkel));
        huhn.add(polygon(new double[] {22, 40, 0, 32, 8, 34, 2, 46, 22, 50}, Color.black));

        // Beine
        huhn.add(new Rectangle(38, 64, 4, 14, orange));
        huhn.add(new Rectangle(50, 64, 4, 14, orange));

        // Koerper und Bauch
        huhn.add(new Ellipse(14, 25, 62, 46, pFarbe));
        huhn.add(new Ellipse(32, 44, 40, 24, hell));

        // Kopf mit Kamm, Auge, Schnabel und Kehllappen
        huhn.add(new Circle(67, 0, 5, rot));
        huhn.add(new Circle(75, -4, 5, rot));
        huhn.add(new Circle(83, 0, 5, rot));
        huhn.add(new Circle(62, 6, 17, pFarbe));
        huhn.add(new Ellipse(88, 30, 8, 12, rot));
        huhn.add(polygon(new double[] {92, 18, 108, 24, 92, 30}, orange));
        huhn.add(new Circle(78, 12, 6, Color.white));
        huhn.add(new Circle(83, 15, 3, Color.black));

        // Fluegel, abwechselnd sichtbar
        fluegelOben = polygon(new double[] {28, 42, 64, 40, 60, 14, 54, 20, 50, -2, 44, 12, 38, -6, 34, 12, 24, 4}, dunkel);
        fluegelUnten = polygon(new double[] {28, 44, 64, 44, 60, 68, 54, 62, 50, 82, 44, 68, 38, 80, 34, 66, 24, 72}, dunkel);
        huhn.add(fluegelOben);
        huhn.add(fluegelUnten);
        fluegelUnten.setHidden(true);

        // Je nach Entfernung verkleinern, nach links fliegende Huehner spiegeln
        huhn.scale(faktor, faktor);
        if (richtung == -1)
            huhn.flipHorizontal();

        neuStarten();
    }

    // pPunkte = {x1, y1, x2, y2, ...}, add erwartet die Punkte relativ zum ersten Punkt
    Polygon polygon(double[] pPunkte, Color pFarbe)
    {
        Polygon p = new Polygon(pPunkte[0], pPunkte[1], pFarbe);
        for (int i = 2; i < pPunkte.length; i += 2)
            p.add(pPunkte[i] - pPunkte[0], pPunkte[i + 1] - pPunkte[1]);
        return p;
    }

    // Fliegt auf einer Wellenlinie, pFaktor macht das Huhn schneller (Schwierigkeit)
    void bewege(double pFaktor)
    {
        // Abgeschossen: kopfueber nach unten fallen
        if (getroffen)
        {
            fallTempo = fallTempo + 0.6;
            huhn.move(0, fallTempo);
            return;
        }

        welleWinkel = welleWinkel + 0.04;
        huhn.move(tempo * pFaktor * richtung, Math.sin(welleWinkel) * welleHoehe);

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
        if (getroffen)
            return huhn.getShapeY() > 800;
        if (richtung == 1)
            return huhn.getShapeX() > 1600;
        return huhn.getShapeX() + huhn.getShapeWidth() < 0;
    }

    // Startet ausserhalb des Fensters mit zufaelligem Abstand, damit nicht alle gleichzeitig kommen
    void neuStarten()
    {
        if (getroffen)
        {
            huhn.turn(180);
            getroffen = false;
        }

        int neuY = Tools.randomNumber(flugHoehe, flugHoehe + 250);
        int abstand = Tools.randomNumber(0, 1500);
        if (richtung == 1)
            huhn.moveTo(-huhn.getShapeWidth() - abstand, neuY);
        else
            huhn.moveTo(1600 + abstand, neuY);

        welleWinkel = 0;
        welleHoehe = Tools.randomNumber(5, 20) / 10.0;
    }

    int gibPunkte()
    {
        return punkte;
    }

    // Ein Huhn, das schon faellt, kann nicht nochmal getroffen werden
    boolean trifft(Circle pTreffpunkt)
    {
        return !getroffen && huhn.intersects(pTreffpunkt);
    }

    void abschiessen()
    {
        getroffen = true;
        fallTempo = -4;
        huhn.turn(180);
        fluegelOben.setHidden(false);
        fluegelUnten.setHidden(true);
    }
}
