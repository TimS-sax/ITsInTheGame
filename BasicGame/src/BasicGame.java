import nl.saxion.app.SaxionApp;

import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;

import java.awt.*;
import java.util.ArrayList;

public class BasicGame implements GameLoop {
    int fouten = 0;

    String[] galgstappen = {
            "50,50,100,100",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9",
            "10",
            "11",
            "12",
            "13"
            // maak format x1,y1,x2,y2
    };

    public static void main(String[] args) {
        SaxionApp.startGameLoop(new BasicGame(), 1280, 775, 40);
    }

    public void background() {
        SaxionApp.drawImage("BasicGame/resources/background.jpg", 0, 0, 1280, 775);
    }

    public void startScreen() {
        // Game maker credits
        SaxionApp.setFill(Color.white);
        SaxionApp.setBorderSize(0);
        SaxionApp.drawBorderedText("Game makers: Joshua, Kjeld, Tim & Mats", 5, 760, 11);

        // Titel muziek
        SaxionApp.playSound("BasicGame/resources/background music.wav");

        // Game titel
        SaxionApp.setFill(Color.white);
        SaxionApp.setBorderSize(10);
        SaxionApp.setBorderColor(Color.black);
        SaxionApp.drawBorderedText("Hangen maar!", 150, 100, 150);

        SaxionApp.setFill(Color.white);
        SaxionApp.setBorderColor(Color.black);
        SaxionApp.setBorderSize(3);

        SaxionApp.drawRectangle(250,440,130,40);
        SaxionApp.drawRectangle(245,540,155,40);
        SaxionApp.drawRectangle(220,640,250,40);

        SaxionApp.drawRectangle(560,440,95,40);
        SaxionApp.drawRectangle(555,540,105,40);
        SaxionApp.drawRectangle(555,640,105,40);

        SaxionApp.drawRectangle(820,440,205,40);
        SaxionApp.drawRectangle(845,540,100,40);
        SaxionApp.drawRectangle(810,640,230,40);

        SaxionApp.setBorderSize(1);
        SaxionApp.setFill(Color.black);
        SaxionApp.setBorderColor(Color.black);
        // De 9 thema's met de boxen eromheen
        SaxionApp.drawBorderedText("Beroepen", 260, 450, 25);
        SaxionApp.drawBorderedText("Dieren", 570, 450, 25);
        SaxionApp.drawBorderedText("Eten en Drinken",830, 450, 25);

        SaxionApp.drawBorderedText("Feestdagen", 255, 550, 25);
        SaxionApp.drawBorderedText("Kleuren", 565, 550, 25);
        SaxionApp.drawBorderedText("Landen", 855, 550, 25);

        SaxionApp.drawBorderedText("Planten en Bloemen", 230, 650, 25);
        SaxionApp.drawBorderedText("Sporten", 565, 650, 25);
        SaxionApp.drawBorderedText("Transportmiddelen", 820, 650, 25);

    }

    public void galgUI() {
        for (int i = 0; i <= fouten; i++) {
            //run hier de stappen voor het tekenen van de galg, i is welke stap je moet hebben
            String[] coordinates = galgstappen[i].split(",");
            int x1 = Integer.parseInt(coordinates[0]);
            int y1 = Integer.parseInt(coordinates[1]);
            int x2 = Integer.parseInt(coordinates[2]);
            int y2 = Integer.parseInt(coordinates[3]);
            SaxionApp.drawLine(x1, y1, x2, y2);
        }
    }

    public void foutGok() {
        if (fouten < 13) {
            fouten++;
        } else if (fouten == 13) {
           //verlies conditie
        }
        galgUI();
    }

    @Override
    public void init() {
        background();
        startScreen();
    }

    @Override
    public void loop() {

    }

    @Override
    public void keyboardEvent(KeyboardEvent keyboardEvent) {

    }

    @Override
    public void mouseEvent(MouseEvent mouseEvent) {

    }

    public void teringzooi() {
        SaxionApp.print("Hoeveel letters bevat het woord?: ");
        int aantalletters = SaxionApp.readInt();
        SaxionApp.print("Dit is het woord: ");
        ArrayList<Character> naam = new ArrayList<>();
        ArrayList<Character> user = new ArrayList<>();
        ArrayList<Character> verkeerd = new ArrayList<>();

        for (int tel = 0; tel < aantalletters; tel++) {
            char letter = SaxionApp.readChar();
            naam.add(letter);
            SaxionApp.print(letter);
        }

        SaxionApp.pause();
        SaxionApp.clear();

        SaxionApp.printLine("Het woord bevat " + aantalletters + " letters");
        SaxionApp.print("Gok een letter: ");
        char gok = SaxionApp.readChar();
        SaxionApp.print(gok);
        int goed = 0;
        int fout = 0;
        boolean loop = true;

        while (loop) {

            if (naam.contains(gok) && !user.contains(gok)) {
                SaxionApp.printLine();
                SaxionApp.printLine("De gok was goed!", Color.green);
                user.add(gok);
                goed++;
                SaxionApp.print("Dit zijn je verkeerde gegokte letters: ");
                SaxionApp.print(verkeerd);
                SaxionApp.printLine();
                SaxionApp.printLine();

                if (user.size() == aantalletters) {
                    SaxionApp.printLine("Je hebt het woord goed gegokt!", Color.cyan);
                    SaxionApp.printLine("Het woord was " + naam);
                    SaxionApp.printLine();
                    loop = false;

                } else {
                    SaxionApp.print("Doe nog een gok: ");
                    gok = SaxionApp.readChar();
                    SaxionApp.print(gok);
                }
            } else if (!naam.contains(gok)) {
                SaxionApp.printLine();
                SaxionApp.printLine("De gok was fout!", Color.red);
                verkeerd.add(gok);
                fout++;
                SaxionApp.printLine("Je hebt nu " + fout + " fouten");
                SaxionApp.print("Dit zijn je gegokte verkeerde letters: ");
                SaxionApp.print(verkeerd);
                SaxionApp.printLine();
                SaxionApp.printLine();
                SaxionApp.print("Doe nog een gok: ");
                gok = SaxionApp.readChar();
                SaxionApp.print(gok);
            } else if (user.contains(gok)) {
                SaxionApp.printLine();
                SaxionApp.printLine("Je hebt deze letter al gegokt!", Color.yellow);
                SaxionApp.printLine();
                SaxionApp.print("Dit zijn je gegokte letters: ");
                SaxionApp.print(user);
                SaxionApp.print("Doe een nieuwe gok: ");
                gok = SaxionApp.readChar();
                SaxionApp.print(gok);
            }
        }
    }
}






