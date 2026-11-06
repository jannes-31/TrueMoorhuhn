import sas.*;
import java.awt.Color;

public class Welt
{
    Rectangle[] himmel;
    Circle sonneAussen, sonneInnen;
    Polygon berg1, berg2, berg3, berg4;
    Ellipse huegel1, huegel2, huegel3;
    Rectangle boden;
    Color grasFarbe1, grasFarbe2, grasFarbe3, bergFarbe1, bergFarbe2, wolkenFarbe;

    Welt()
    {
        grasFarbe1 = new Color(200, 225, 45);
        grasFarbe2 = new Color(160, 205, 60);
        grasFarbe3 = new Color(125, 185, 70);
        bergFarbe1 = new Color(110, 185, 195);
        bergFarbe2 = new Color(65, 155, 190);
        wolkenFarbe = new Color(250, 248, 215);

        // Himmel als Farbverlauf aus Streifen
        himmel = new Rectangle[20];
        for (int i = 0; i < himmel.length; i++)
        {
            int r = 105 + i * 4;
            int g = 200 + i;
            int b = 230 - i;
            himmel[i] = new Rectangle(0, i * 40, 1600, 40, new Color(r, g, b));
        }

        sonneAussen = new Circle(780, 330, 120, new Color(250, 225, 50));
        sonneInnen = new Circle(830, 380, 70, new Color(255, 240, 120));

        wolke(120, 240, 600);
        wolke(980, 110, 520);
        wolke(30, 470, 330);
        wolke(1080, 450, 400);

        // Berge
        berg1 = new Polygon(0, 700, bergFarbe1);
        berg1.add(220, -190);
        berg1.add(380, -120);
        berg1.add(560, -280);
        berg1.add(800, 0);

        berg2 = new Polygon(700, 700, bergFarbe1);
        berg2.add(260, -250);
        berg2.add(420, -140);
        berg2.add(620, -330);
        berg2.add(900, 0);

        berg3 = new Polygon(300, 700, bergFarbe2);
        berg3.add(280, -160);
        berg3.add(520, 0);

        berg4 = new Polygon(950, 700, bergFarbe2);
        berg4.add(380, -190);
        berg4.add(650, 0);

        // Huegel
        huegel1 = new Ellipse(-200, 560, 1100, 400);
        huegel1.setColor(grasFarbe3);

        huegel2 = new Ellipse(700, 590, 1200, 380);
        huegel2.setColor(grasFarbe2);

        huegel3 = new Ellipse(-300, 660, 2200, 400);
        huegel3.setColor(grasFarbe1);

        boden = new Rectangle(0, 760, 1600, 40, grasFarbe1);
    }

    void wolke(double pX, double pY, double pBreite)
    {
        double h = pBreite / 4;
        new Ellipse(pX, pY + h * 0.6, pBreite, h * 0.8, wolkenFarbe);
        new Circle(pX + pBreite * 0.15, pY + h * 0.2, h * 0.5, wolkenFarbe);
        new Circle(pX + pBreite * 0.35, pY - h * 0.3, h * 0.75, wolkenFarbe);
        new Circle(pX + pBreite * 0.6, pY, h * 0.6, wolkenFarbe);
    }
}
