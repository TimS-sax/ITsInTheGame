import nl.saxion.app.SaxionApp;

import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class BasicGame implements GameLoop {
    int fouten = 0;

    String[] galgstappen = {
            "250,200,300,200", // "_"
            "250,200,275,175", // "/"
            "300,200,275,175", // "\"
            "275,175,275,125", // "|"
            "275,125,325,125", // "-"
            "275,135,285,125", // "/"
            "325,125,325,140", // "|"
            "circle", // "325,145,5"
            "325,150,325,160", // "|"
            "325,155,315,145", // "\"
            "325,155,335,145", // "/"
            "325,160,315,165", // "/"
            "325,160,335,165", // "\"
            // maak format x1,y1,x2,y2

    };

    public static void main(String[] args) {
        SaxionApp.startGameLoop(new BasicGame(), 1280, 775, 40);
    }

    public void background() {
        SaxionApp.drawImage("BasicGame/resources/background.jpg", 0, 0, 1280, 775);
    }

    public void startScreen() {
        // Titel muziek
        SaxionApp.playSound("BasicGame/resources/background music.wav");

        // Game maker credits
        SaxionApp.setFill(Color.white);
        SaxionApp.setBorderSize(0);
        SaxionApp.drawBorderedText("Game makers: Joshua, Kjeld, Tim & Mats", 5, 760, 11);

        // Game titel
        SaxionApp.setFill(Color.white);
        SaxionApp.setBorderSize(10);
        SaxionApp.setBorderColor(Color.black);
        SaxionApp.drawBorderedText("Hangen maar!", 150, 100, 150);

        SaxionApp.setFill(Color.white);
        SaxionApp.setBorderColor(Color.black);
        SaxionApp.setBorderSize(3);

        // Vakjes keuze scherm om de thema's heen
        SaxionApp.drawRectangle(250, 440, 130, 40);
        SaxionApp.drawRectangle(245, 540, 155, 40);
        SaxionApp.drawRectangle(220, 640, 250, 40);

        SaxionApp.drawRectangle(560, 440, 95, 40);
        SaxionApp.drawRectangle(555, 540, 105, 40);
        SaxionApp.drawRectangle(555, 640, 105, 40);

        SaxionApp.drawRectangle(820, 440, 205, 40);
        SaxionApp.drawRectangle(845, 540, 100, 40);
        SaxionApp.drawRectangle(810, 640, 230, 40);

        SaxionApp.setBorderSize(1);
        SaxionApp.setFill(Color.black);
        SaxionApp.setBorderColor(Color.black);
        // De 9 thema's in tekst
        SaxionApp.drawBorderedText("Beroepen", 260, 450, 25);
        SaxionApp.drawBorderedText("Dieren", 570, 450, 25);
        SaxionApp.drawBorderedText("Eten en Drinken", 830, 450, 25);

        SaxionApp.drawBorderedText("Feestdagen", 255, 550, 25);
        SaxionApp.drawBorderedText("Kleuren", 565, 550, 25);
        SaxionApp.drawBorderedText("Landen", 855, 550, 25);

        SaxionApp.drawBorderedText("Planten en Bloemen", 230, 650, 25);
        SaxionApp.drawBorderedText("Sporten", 565, 650, 25);
        SaxionApp.drawBorderedText("Transportmiddelen", 820, 650, 25);

    }

    public void galgUI() {
        for (int i = 0; i <= fouten; i++) {
            //run hier de stappen voor het tekenen van de galg, i is welke stap je moet
            if (Objects.equals(galgstappen[i], "circle")) {
                SaxionApp.drawCircle(325,140,5);
            } else {
                String[] coordinates = galgstappen[i].split(",");
                int x1 = Integer.parseInt(coordinates[0]);
                int y1 = Integer.parseInt(coordinates[1]);
                int x2 = Integer.parseInt(coordinates[2]);
                int y2 = Integer.parseInt(coordinates[3]);
                SaxionApp.drawLine(x1, y1, x2, y2);
            }
        }
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
        if (mouseEvent.isMouseDown()) {
            if (mouseEvent.isLeftMouseButton()) {
                int mouseX = mouseEvent.getX();
                int mouseY = mouseEvent.getY();

                handleMouseClick(mouseX, mouseY);
            }
        }
    }

    // Output na het klikken van de vakjes van het keuze scherm
    private void handleMouseClick(int mouseX, int mouseY) {

        if (isInsideRectangle(mouseX, mouseY, 250, 440, 130, 40)) {
            System.out.println("Beroepen");
        } else if (isInsideRectangle(mouseX, mouseY, 245, 540, 155, 40)) {
            System.out.println("Feestdagen");
        } else if (isInsideRectangle(mouseX, mouseY, 220, 640, 250, 40)) {
            System.out.println("Planten en Bloemen");
        } else if (isInsideRectangle(mouseX, mouseY, 560, 440, 95, 40)) {
            System.out.println("Dieren");
        } else if (isInsideRectangle(mouseX, mouseY, 555, 540, 105, 40)) {
            System.out.println("Kleuren");
        } else if (isInsideRectangle(mouseX, mouseY, 555, 640, 105, 40)) {
            System.out.println("Sporten");
        } else if (isInsideRectangle(mouseX, mouseY, 820, 440, 205, 40)) {
            System.out.println("Eten en Drinken");
        } else if (isInsideRectangle(mouseX, mouseY, 845, 540, 100, 40)) {
            System.out.println("Landen");
        } else if (isInsideRectangle(mouseX, mouseY, 810, 640, 230, 40)) {
            System.out.println("Transportmiddelen");
        }
    }

    // Kijkt of de muis in de vakjes van het keuze scherm zitten
    private boolean isInsideRectangle(int mouseX, int mouseY, int rectX, int rectY, int rectWidth, int rectHeight) {
        return mouseX >= rectX && mouseX <= rectX + rectWidth && mouseY >= rectY && mouseY <= rectY + rectHeight;
    }


    // ALle integers en arraylists voor beurten en goedoffout
    public static int goed = 0;
    public static ArrayList<Character> naam = new ArrayList<>();
    public static ArrayList<Character> user = new ArrayList<>();
    public static ArrayList<Character> verkeerd = new ArrayList<>();
    public static int aantalletters;

    public void woord() {
        SaxionApp.print("Hoeveel letters bevat het woord?: ");
        aantalletters = SaxionApp.readInt();
        SaxionApp.print("Dit is het woord: ");
        for (int tel = 0; tel < aantalletters; tel++) {
            char letter = SaxionApp.readChar();
            naam.add(letter);
            SaxionApp.print(letter);
        }
    }

        public void goedoffout () {
            SaxionApp.pause();
            SaxionApp.clear();
            SaxionApp.printLine("Het woord bevat " + aantalletters + " letters");
            SaxionApp.print("Gok een letter: ");
            char gok = SaxionApp.readChar();
            SaxionApp.print(gok);
            int fout = 0;
            boolean loop = true;

            while (loop) {
                if (naam.contains(gok)) {
                    int countCorrect = 0;

                    for (int i = 0; i < naam.size(); i++) {
                        if (naam.get(i) == gok && !user.contains(gok)) {
                            user.add(gok);
                            goed++;
                            countCorrect++;
                        }
                    }

                    if (countCorrect > 0) {
                        SaxionApp.printLine();
                        SaxionApp.printLine("De gok was goed!", Color.green);
                        SaxionApp.print("Dit zijn je verkeerd gegokte letters: ");
                        SaxionApp.print(verkeerd);
                        SaxionApp.printLine();
                    }

                    if (goed == aantalletters) {
                        SaxionApp.printLine("Je hebt het woord goed gegokt!", Color.cyan);
                        SaxionApp.printLine("Het woord was: " + naam);
                        loop = false;
                    } else {
                        SaxionApp.print("Doe nog een gok: ");
                        gok = SaxionApp.readChar();
                        SaxionApp.print(gok);
                    }
                } else if (!naam.contains(gok)) {
                    SaxionApp.printLine();
                    SaxionApp.printLine("De gok was fout!", Color.red);
                    if (!verkeerd.contains(gok)) {
                        verkeerd.add(gok);
                        fout++;
                    }

                    if (fout < 13) {
                        SaxionApp.printLine("Je hebt nu " + fout + " fouten", Color.red);
                        SaxionApp.print("Dit zijn je gegokte verkeerde letters: ");
                        SaxionApp.print(verkeerd);
                        SaxionApp.printLine();
                        SaxionApp.print("Doe nog een gok: ");
                        gok = SaxionApp.readChar();
                        SaxionApp.print(gok);
                    } else {
                        SaxionApp.printLine("Je bent af!", Color.red);
                        loop = false;
                    }
                } else {
                    SaxionApp.printLine("Je hebt deze letter al correct gegokt!", Color.yellow);
                    SaxionApp.print("Doe een nieuwe gok: ");
                    gok = SaxionApp.readChar();
                    SaxionApp.print(gok);
                }
            }
        }
    }

