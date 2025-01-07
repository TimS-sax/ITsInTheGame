import nl.saxion.app.CsvReader;
import nl.saxion.app.SaxionApp;
import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Objects;

public class BasicGame implements GameLoop {

    // ================================================
    // Velden en Constanten
    // ================================================

    private boolean toonThemaScherm = false;
    private boolean toonSpelScherm = false;
    private int fouten = 0;
    public static String huidigThema = "";
    private ArrayList<String> toetsaanslagen = new ArrayList<>();
    private boolean volgToetsaanslagen = false;

    private String[] galgStappen = {
            "250,200,300,200", // "_"
            "250,200,275,175", // "/"
            "300,200,275,175", // "\"
            "275,175,275,125", // "|"
            "275,125,325,125", // "-"
            "275,135,285,125", // "/"
            "325,125,325,140", // "|"
            "circle",          // "325,145,5"
            "325,150,325,160", // "|"
            "325,155,315,145", // "\"
            "325,155,335,145", // "/"
            "325,160,315,165", // "/"
            "325,160,335,165"  // "\"
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
        if (toonThemaScherm) {
            maakThemaScherm();
        } else if (toonSpelScherm) {
            maakSpelScherm();
        }
    }

    @Override
    public void keyboardEvent(KeyboardEvent toetsEvent) {
        if (volgToetsaanslagen && toetsEvent.isKeyPressed()) {
            String letter = toetsCodeNaarLetter(toetsEvent.getKeyCode());

            toetsaanslagen.add(letter);
            System.out.println(toetsaanslagen);
        }
    }

    @Override
    public void mouseEvent(MouseEvent muisEvent) {
        if (muisEvent.isMouseDown() && muisEvent.isLeftMouseButton()) {
            int muisX = muisEvent.getX();
            int muisY = muisEvent.getY();

            if (!toonThemaScherm && !toonSpelScherm) {
                if (isBinnenRechthoek(muisX, muisY, 390, 400, 200, 60)) {
                    toonThemaScherm = true;
                } else if (isBinnenRechthoek(muisX, muisY, 690, 400, 200, 60)) {
                    SaxionApp.quit();
                }
            } else if (toonThemaScherm) {
                for (int i = 0; i < themaVertaling.length; i++) {
                    int x = 200 + (i % 3) * 300;
                    int y = 150 + (i / 3) * 100;
                    if (isBinnenRechthoek(muisX, muisY, x + 30, y + 200, 200, 60)) {
                        huidigThema = themaVertaling[i];
                        System.out.println("Thema geselecteerd: " + huidigThema);
                        woordenLijst.clear();
                        laadWoorden(huidigThema);
                        toonThemaScherm = false;
                        toonSpelScherm = true;
                    }
                }
            } else if (toonSpelScherm) {
                if (isBinnenRechthoek(muisX, muisY, 1000, 680, 200, 60)) {
                    toonSpelScherm = false;
                    maakStartMenu();
                }
            }
        }
    }

    // ================================================
    // UI Methoden
    // ================================================

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
        SaxionApp.clear();
        SaxionApp.drawImage("BasicGame/resources/background.jpg", 0, 0, 1280, 775);
        SaxionApp.setBorderColor(Color.white);
        SaxionApp.drawText("Thema: " + huidigThema, 50, 50, 30);
        SaxionApp.drawText("Fouten: " + fouten, 50, 100, 30);
        tekenGalg();
        maakKnop(1000, 680, 200, 60, "Terug naar Menu");
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
                SaxionApp.drawCircle(325, 140, 5);
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

    private boolean checkGoedOfFout(char gok, String woord) {
        String[] gesplitswoord = woord.split("");
        return IsCharInStringArray(gok, gesplitswoord);
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
        return KeyEvent.getKeyText(toetsCode);
    }

    private void laadWoorden(String thema) {
        CsvReader lezer = new CsvReader("BasicGame/resources/" + thema + ".csv");
        while (lezer.loadRow()) {
            String woord = lezer.getString(0);
            woordenLijst.add(woord);
        }
    }
}
