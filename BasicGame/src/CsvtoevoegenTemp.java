import java.io.FileWriter;
import java.io.IOException;

public class CsvtoevoegenTemp {
    // stuur data
    public static void appendRowToCSV(String filePath, String[] newRow) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            for (int i = 0; i < newRow.length; i++) {
                writer.append(newRow[i]);
                if (i < newRow.length - 1) {
                    writer.append(",");
                }
            }
            writer.append("\n");

            System.out.println("Data gestuurd");
        } catch (IOException e) {
            System.err.println("error: " + e.getMessage());
        }
    }

    // Overloaded method for appending an integer score
    public static void appendRowToCSV(String filePath, int score) {
        appendRowToCSV(filePath, new String[] { String.valueOf(score) });
    }

    // Save an integer score to the CSV file
    public static void csvopslaanT(int score) {
        appendRowToCSV("tempscore.csv", score); // File path hardcoded
    }
}
