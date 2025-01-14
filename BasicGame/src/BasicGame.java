import nl.saxion.app.CsvReader;
import nl.saxion.app.SaxionApp;
import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class BasicGame implements GameLoop {

    // ================================================
    // Velden en Constanten
    // ================================================

    private char character;

    ArrayList<String> hashedWoord = new ArrayList<>();

    private String huidigScherm = "start"; // Possible values: "start", "thema", "spel"
    private int fouten = 0;
    public static String huidigThema = "";
    private ArrayList<String> toetsaanslagen = new ArrayList<>();
    private boolean volgToetsaanslagen = false;
    private boolean genereerWoord = true;
    private String randomWoord = "";
    private String[] willekeurigWoordArray = {};
    private int einde = 0;
    private int eind = 0;
    private String[] galgStappen = {
            "375,600,450,600", // "_"
            "375,600,412,562", // "/"
            "450,600,412,562", // "\"
            "412,562,412,487", // "|"
            "412,487,487,487", // "-"
            "412,502,427,487", // "/"
            "487,487,487,510", // "|"
            "circle",          // "487,517,7.5"
            "487,525,487,540", // "|"
            "487,532,472,517", // "\"
            "487,532,502,517", // "/"
            "487,540,472,547", // "/"
            "487,540,502,547"  // "\"
    };

    private String[] themas = {
            "Dieren", "Beroepen", "Eten en Drinken", "Feestdagen",
            "Kleuren", "Landen", "Planten en Bloemen", "Sporten", "Transportmiddelen"
    };

    private String[] themaVertaling = {
            "Dieren", "Beroepen", "Etenendrinken", "Feestdagen",
            "Kleuren", "Landen", "Plantenenbloemen", "Sporten", "Transportmiddelen"
    };

    private static final ArrayList<String> woordenLijst = new ArrayList<>();
    private static final ArrayList<Character> naam = new ArrayList<>();
    private static final ArrayList<Character> gebruikerGok = new ArrayList<>();
    private static final ArrayList<Character> verkeerdeGok = new ArrayList<>();
    private static final ArrayList<String> onthultWoord = new ArrayList<>();

    private static int juisteGokken = 0;
    private static int aantalLetters;
    private int punten = 13;

    // ================================================
    // Hoofd Methode
    // ================================================

    public static void main(String[] args) {
        SaxionApp.startGameLoop(new BasicGame(), 1280, 775, 40);
    }

    // ================================================
    // GameLoop Interface Methoden
    // ================================================

    @Override
    public void init() {
        SaxionApp.playSound("BasicGame/resources/background_music.wav");
        maakStartMenu();
    }

    @Override
    public void loop() {
        switch (huidigScherm) {
            case "start":
                maakStartMenu();
                break;
            case "thema":
                maakThemaScherm();
                break;
            case "spel":
                maakSpelScherm();
                break;
            case "eind":
                if (fouten == 12) {
                    boolean wincon = false;
                    maakEindScherm(wincon);
                }
            default:
                System.out.println("Onbekend scherm: " + huidigScherm);
                break;
        }
    }

    @Override
    public void keyboardEvent(KeyboardEvent toetsEvent) {
        if (volgToetsaanslagen && toetsEvent.isKeyPressed()) {
            String letter = toetsCodeNaarLetter(toetsEvent.getKeyCode());
            character = letter.charAt(0);
            checkGoedOfFout(character,willekeurigWoordArray);
            toetsaanslagen.add(letter.toLowerCase());
            System.out.println(toetsaanslagen);
            if(checkGoedOfFout(character,willekeurigWoordArray)) {
                hashWoord(willekeurigWoordArray,character);
            } else if (!checkGoedOfFout(character,willekeurigWoordArray)) {
                tekenGalg();
                fouten++;
            }
            if(fouten == 12) {
                huidigScherm = "eind";
            }
        }
    }

    @Override
    public void mouseEvent(MouseEvent muisEvent) {
        if (muisEvent.isMouseDown() && muisEvent.isLeftMouseButton()) {
            int muisX = muisEvent.getX();
            int muisY = muisEvent.getY();

            switch (huidigScherm) {
                case "start":
                    handleStartMenuMouse(muisX, muisY);
                    break;
                case "thema":
                    handleThemaSchermMouse(muisX, muisY);
                    break;
                case "spel":
                    handleSpelSchermMouse(muisX, muisY);
                    break;
                default:
                    System.out.println("Onbekende mouseEvent op scherm: " + huidigScherm);
                    break;
            }
        }
    }

    private void handleStartMenuMouse(int muisX, int muisY) {
        if (isBinnenRechthoek(muisX, muisY, 390, 400, 200, 60)) {
            huidigScherm = "thema"; // Switch to theme screen
        } else if (isBinnenRechthoek(muisX, muisY, 690, 400, 200, 60)) {
            SaxionApp.quit(); // Quit the game
        }
    }

    private void handleThemaSchermMouse(int muisX, int muisY) {
        for (int i = 0; i < themaVertaling.length; i++) {
            int x = 200 + (i % 3) * 300;
            int y = 150 + (i / 3) * 100;
            if (isBinnenRechthoek(muisX, muisY, x + 30, y + 200, 200, 60)) {
                huidigThema = themaVertaling[i];
                System.out.println("Thema geselecteerd: " + huidigThema);
                woordenLijst.clear();
                laadWoorden(huidigThema);
                huidigScherm = "spel"; // Switch to gameplay screen
            }
        }
    }

    private void handleSpelSchermMouse(int muisX, int muisY) {
        if (isBinnenRechthoek(muisX, muisY, 1000, 680, 200, 60)) {
            SaxionApp.quit(); // Quit the game
        }
    }


    // ================================================
    // UI Methoden
    // ================================================

    private void maakEindScherm(boolean wincon) {
        SaxionApp.clear();
        SaxionApp.drawImage("BasicGame/resources/background.jpg", 0, 0, 1280, 775);
        SaxionApp.setFill(Color.white);
        SaxionApp.setBorderColor(Color.black);
        SaxionApp.setBorderSize(5);
        if(!wincon) {
            SaxionApp.drawBorderedText("Je hebt verloren!", 150,150,100);
        } else if (wincon) {
            SaxionApp.drawBorderedText("Gewonnen, geweldig!", 150,150, 100);
        }
    }

    private void maakStartMenu() {
        SaxionApp.clear();
        SaxionApp.drawImage("BasicGame/resources/background.jpg", 0, 0, 1280, 775);
        SaxionApp.setFill(Color.white);
        SaxionApp.setBorderColor(Color.black);
        SaxionApp.setBorderSize(5);
        SaxionApp.drawBorderedText("Hangen Maar!", 170, 150, 150);
        maakKnop(390, 400, 200, 60, "Thema's");
        maakKnop(690, 400, 200, 60, "Stop Spel");
    }

    private void maakThemaScherm() {
        SaxionApp.clear();
        SaxionApp.drawImage("BasicGame/resources/background.jpg", 0, 0, 1280, 775);
        for (int i = 0; i < themas.length; i++) {
            int x = 200 + (i % 3) * 300;
            int y = 150 + (i / 3) * 100;
            maakKnop(x + 30, y + 200, 200, 60, themas[i]);
        }
    }

    private void maakSpelScherm() {
        volgToetsaanslagen = true;
        if (genereerWoord) {
            willekeurigWoordArray = randomWoord();
            hashedWoord = hashWoord(willekeurigWoordArray,character);
            System.out.println(Arrays.toString(willekeurigWoordArray));
            System.out.println(willekeurigWoordArray.length);
            genereerWoord = false;

        }
        SaxionApp.clear();
        SaxionApp.drawImage("BasicGame/resources/background.jpg", 0, 0, 1280, 775);
        SaxionApp.setBorderColor(Color.white);
        SaxionApp.drawText("Thema: " + huidigThema, 50, 50, 30);
        SaxionApp.drawText("Fouten: " + fouten, 50, 100, 30);
        SaxionApp.drawText("Woord Lengte: " + willekeurigWoordArray.length, 50, 150, 30);
        SaxionApp.drawText(String.valueOf(hashedWoord),600,50,30);
        tekenGalg();
        maakKnop(1000, 680, 200, 60, "Stop Spel");
    }

    private void maakKnop(int x, int y, int breedte, int hoogte, String tekst) {
        SaxionApp.setFill(Color.white);
        SaxionApp.setBorderColor(Color.black);
        SaxionApp.setBorderSize(2);
        SaxionApp.drawRectangle(x, y, breedte, hoogte);
        SaxionApp.setFill(Color.black);
        SaxionApp.drawText(tekst, x + (breedte - tekst.length() * 10) / 2, y + (hoogte - 10) / 2, 20);
    }

    private void tekenGalg() {
        for (int i = 0; i <= fouten; i++) {
            if (Objects.equals(galgStappen[i], "circle")) {
                SaxionApp.drawCircle(487, 517, 7); // Scaled center and radius
            } else {
                String[] coördinaten = galgStappen[i].split(",");
                int x1 = Integer.parseInt(coördinaten[0]);
                int y1 = Integer.parseInt(coördinaten[1]);
                int x2 = Integer.parseInt(coördinaten[2]);
                int y2 = Integer.parseInt(coördinaten[3]);
                SaxionApp.drawLine(x1, y1, x2, y2);
            }
        }
    }

    // ================================================
    // Hulpmethoden
    // ================================================

    private boolean checkGoedOfFout(char gok, String[] woord) {
        return IsCharInStringArray(gok, woord);
    }

    private boolean IsCharInStringArray(char character, String[] array) {
        for (String string : array) {
            if (string.equals(String.valueOf(character))) {
                return true;
            }
        }
        return false;
    }

    private boolean isBinnenRechthoek(int muisX, int muisY, int rechthoekX, int rechthoekY, int breedte, int hoogte) {
        return muisX >= rechthoekX && muisX <= rechthoekX + breedte && muisY >= rechthoekY && muisY <= rechthoekY + hoogte;
    }

    private String toetsCodeNaarLetter(int toetsCode) {
        return KeyEvent.getKeyText(toetsCode).toLowerCase();
    }

    private void laadWoorden(String thema) {
        CsvReader lezer = new CsvReader("BasicGame/resources/" + thema + ".csv");
        while (lezer.loadRow()) {
            String woord = lezer.getString(0);
            woordenLijst.add(woord);
        }
    }

    private String[] randomWoord() {
        int randomNummer = (int) (Math.random() * BasicGame.woordenLijst.size());
        randomWoord = BasicGame.woordenLijst.get(randomNummer);
        System.out.println("Woord geselecteerd: " + randomWoord);
        return randomWoord.split("");
    }

    //csvreader voor tempscore
    private void Tempcsvlezen(String data) {
        CsvReader tdatalezen = new CsvReader("Basicgame/resources/tempscore.csv");

        while (tdatalezen.loadRow()) {
            int waarde = tdatalezen.getInt(0);
            int eindscore = eind + waarde;
        }
    }

    //score uitrekenen & onthouden
    private void score(int gamestatus) { // eind scherm rekenen stuff
        int rondescore = 13 - fouten;

        if (gamestatus == 0) { //speler wint
            CsvtoevoegenTemp.csvopslaanT(rondescore);

        } else if (gamestatus == 1) {//speler verliest
//            Tempcsvlezen();
            int eindescore = eind + rondescore; //!!
            CsvtoevoegenHighscore.csvopslaanH(eindescore);
            clearCSV("Basicgame/resources/tempscore.csv");
        }
    }

    private ArrayList<String> hashWoord(String[] woord, char character) {
        // Initialize hashedWoord only if it's empty
        if (hashedWoord.isEmpty()) {
            for (int i = 0; i < woord.length; i++) {
                hashedWoord.add("-");
            }
        }

        // Update only the positions where the guessed character matches
        for (int i = 0; i < woord.length; i++) {
            if (woord[i].equals(String.valueOf(character))) {
                hashedWoord.set(i, String.valueOf(character));
            }
        }

        // Check if the entire word is revealed
        if (!hashedWoord.contains("-")) {
            boolean wincon = true; // Set win condition to true
            huidigScherm = "eind";
            maakEindScherm(wincon);
        }

        System.out.print(hashedWoord);
        return hashedWoord;
    }

//csv file legen
    private void clearCSV(String filePath) {
        try (FileWriter fileWriter = new FileWriter(filePath, false)) {
            fileWriter.write("");
        } catch (IOException e) {
            System.out.println("An error occurred while clearing the file: " + e.getMessage());
        }
    }
}