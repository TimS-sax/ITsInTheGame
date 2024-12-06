import nl.saxion.app.CsvReader;
import nl.saxion.app.SaxionApp;

import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;

import java.awt.*;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Objects;

import java.io.FileWriter;
import java.io.IOException;

public class BasicGame implements GameLoop {
    private boolean laatThemaSchermZien = false;
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

    String[] themas = {
            "Dieren", "Beroepen", "Eten en Drinken", "Feestdagen",
            "Kleuren", "Landen", "Planten en Bloemen", "Sporten", "Transportmiddelen"
    };

    public static void main(String[] args) {
        SaxionApp.startGameLoop(new BasicGame(), 1280, 775, 40);
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
        SaxionApp.playSound("BasicGame/resources/background music.wav");
        maakStartMenu();
        reader();
    }

    @Override
    public void loop() {
        if (laatThemaSchermZien) {
            maakThemaScherm();
        }
    }

    @Override
    public void keyboardEvent(KeyboardEvent keyboardEvent) {

    }

    @Override
    public void mouseEvent(MouseEvent mouseEvent) {
        if (mouseEvent.isMouseDown() && mouseEvent.isLeftMouseButton()) {
            int mouseX = mouseEvent.getX();
            int mouseY = mouseEvent.getY();

            if (!laatThemaSchermZien) {
                // Kijkt of de knop voor het thema scherm is aangeklikt
                if (isInsideRectangle(mouseX, mouseY, 390, 400, 200, 60)) {
                    laatThemaSchermZien = true;
                }
                // Kijkt of de knop om het spel te stoppen is aangeklikt
                if (isInsideRectangle(mouseX, mouseY, 690, 400, 200, 60)) {
                    SaxionApp.quit();
                }
            } else {
                // Kijkt welke knop er is gekozen.
                for (int i = 0; i < themas.length; i++) {
                    int x = 200 + (i % 3) * 300;
                    int y = 150 + (i / 3) * 100;
                    if (isInsideRectangle(mouseX, mouseY, x + 30, y + 200, 200, 60)) {
                        System.out.println("Thema geselecteerd: " + themas[i]);
                    }
                }
            }
        }
    }

    // Begin scherm na het runnen van de applicatie
    private void maakStartMenu() {
        SaxionApp.clear();
        SaxionApp.drawImage("BasicGame/resources/background.jpg", 0, 0, 1280, 775);

        SaxionApp.setFill(Color.white); // Wit
        SaxionApp.setBorderColor(Color.black); // Zwart
        SaxionApp.setBorderSize(5);
        SaxionApp.drawBorderedText("Hangen Maar!", 170, 150, 150);

        maakKnop(390, 400, 200, 60, "Thema's");
        maakKnop(690, 400, 200, 60, "Stop Spel");
    }

    // Dit is het thema scherm wat je ziet na het start scherm
    private void maakThemaScherm() {
        SaxionApp.clear();
        SaxionApp.drawImage("BasicGame/resources/background.jpg", 0, 0, 1280, 775);

        for (int i = 0; i < themas.length; i++) {
            int x = 200 + (i % 3) * 300; // Kolom (3 kolommen)
            int y = 150 + (i / 3) * 100; // Rij
            maakKnop(x + 30, y + 200, 200, 60, themas[i]);
            SaxionApp.setFill(Color.white); // Wit
            SaxionApp.setBorderColor(Color.black); // Zwart
            SaxionApp.setBorderSize(5);
            SaxionApp.drawBorderedText("Hangen Maar!", 170, 150, 150);
        }
    }

    // Hiermee worden de knoppen gemaakt
    private void maakKnop(int x, int y, int width, int height, String text) {
        SaxionApp.setFill(Color.white);
        SaxionApp.setBorderColor(Color.black);
        SaxionApp.setBorderSize(2);
        SaxionApp.drawRectangle(x, y, width, height);

        SaxionApp.setFill(Color.black);
        SaxionApp.drawText(text, x + (width - text.length() * 10) / 2, y + (height - 10) / 2, 20);
    }

    // Kijkt of de muis in de vakjes zitten waar je op kan klikken
    private boolean isInsideRectangle(int mouseX, int mouseY, int rectX, int rectY, int rectWidth, int rectHeight) {
        return mouseX >= rectX && mouseX <= rectX + rectWidth && mouseY >= rectY && mouseY <= rectY + rectHeight;
    }

    // reader
    public static ArrayList woordenlijst = new ArrayList();

    public void reader () {
        CsvReader reader = new CsvReader("BasicGame/resources/beroepen.csv");

        while (reader.loadRow()) {
            String woord = reader.getString(0);
            woordenlijst.add(woord);
        }
        int randomWoord = SaxionApp.getRandomValueBetween(0, woordenlijst.size());
        String randomBeroep = woordenlijst.get(randomWoord).toString();
        System.out.println(randomBeroep);
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




// score uitrekenen
    public void score(int gameStatus) {
        if (gameStatus == 0) { // Player wint
            int finalscore = naam.size() - fouten;

            //stuur data naar csv?



        } else if (gameStatus == 1) { // Player verliest

            //laat totale score zien (score die is opgeslagen + de huidige score)


            //=laat highscore zien

        }
    }





// data naar csv sturen
    public static void appendRowToCSV(String filePath, String[] newRow) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            // nieuwe rij in csv maken
            for (int i = 0; i < newRow.length; i++) {
                writer.append(newRow[i]);
                if (i < newRow.length - 1) {
                    writer.append(","); // , als nodig is
                }
            }
            writer.append("\n"); // nieuwe lijn

            System.out.println("Data appended successfully!");
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    public static void csvtoevoegen(String[] args) {
        // Path to the existing CSV file
        String filePath = "score.csv";

        // Data die moet worden verstuurd
        String[] newRow = {"Jane", "Doe", "30"};

        // Call the method to append data
        appendRowToCSV(filePath, newRow);
        
    }
}

