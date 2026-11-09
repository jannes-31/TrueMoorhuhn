PROJEKTBEZEICHNUNG: TrueMoorhuhn
PROJEKTZWECK: Huehner-Schiessspiel mit der Bibliothek Shapes and Sprites (SaS)
WIE IST DAS PROJEKT ZU STARTEN: Spiel uebersetzen und void main(String[] args) aufrufen,
                                dann ins Fenster klicken, damit es die Tastatur bekommt.

STEUERUNG:
  Pfeiltasten = Fadenkreuz bewegen
  Leertaste   = Schiessen
  R           = Nachladen (1 Sekunde)
  P           = Pause
  Enter       = Start / Neustart

SPIELREGELN:
  90 Sekunden Zeit, 8 Schuss pro Magazin.
  Grosse (nahe) Huehner = 5 Punkte, mittlere = 10, kleine (ferne) = 25.
  Die Huehner werden mit der Zeit schneller.

KLASSEN:
  Spiel       Fenster, Spielschleife, Start-/Endtafel, Pause, Highscore
  Welt        Hintergrund (Himmel, Sonne, Wolken, Berge, Huegel)
  Huhn        Huhn aus Formen, fliegt auf einer Wellenlinie, faellt bei Treffer
  Fadenkreuz  Zielen mit den Pfeiltasten
  Munition    Patronen unten rechts, Nachladen
  Anzeige     Punkte, Schuesse und Restzeit unten links

Die SaS-Bibliothek liegt in +libs.
