import nl.saxion.app.SaxionApp;

import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;

import java.awt.*;
import java.util.ArrayList;

public class BasicGame implements GameLoop {

    public static void main(String[] args) {
        SaxionApp.startGameLoop(new BasicGame(), 1280, 775, 40);
    }

    public void background() {
        SaxionApp.drawImage("BasicGame/resources/background.jpg", 0,0, 1280,775);
    }

    public void startScreen() {
        SaxionApp.setFill(Color.white);
        SaxionApp.setBorderSize(10);
        SaxionApp.setBorderColor(Color.black);
        SaxionApp.drawBorderedText("Hangen maar!", 150, 100, 150);

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

                }else {
                    SaxionApp.print("Doe nog een gok: ");
                    gok = SaxionApp.readChar();
                    SaxionApp.print(gok);
                }
            }

            else if (!naam.contains(gok)) {
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
            }

            else if (user.contains(gok)) {
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






