import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Prompts the user for Product records (ID, Name, Description, Cost), stores
 * each completed record in an ArrayList, and writes the whole list to a
 * comma-delimited text file using BufferedWriter.
 * <p>
 * File format, one record per line, no header:
 * ID, Name, Description, Cost
 *
 * @author Your Name
 * @version 1.0
 */
public class ProductWriter
{
    /**
     * Program entry point.
     * @param args command line arguments, unused
     */
    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        ArrayList<String> records = new ArrayList<>();
        boolean moreData = true;

        System.out.println("===== Product Writer =====");

        while (moreData)
        {
            String id = SafeInput.getRegExString(in, "Enter the 6 digit ID", "\\d{6}");
            String name = SafeInput.getNonZeroLenString(in, "Enter the product name");
            String description = SafeInput.getNonZeroLenString(in, "Enter the description");
            double cost = SafeInput.getDouble(in, "Enter the cost");

            // Only a fully collected record ever reaches the ArrayList
            String record = id + ", " + name + ", " + description + ", " + cost;
            records.add(record);

            System.out.println("\nAdded record: " + record);
            moreData = SafeInput.getYNConfirm(in, "Do you want to enter another product?");
        }

        String fileName = SafeInput.getNonZeroLenString(in, "Enter the file name to save to, e.g. ProductTestData.txt");
        writeFile(fileName, records);
    }

    /**
     * Writes every record to the named text file, one per line, using
     * NIO's BufferedWriter (never Scanner, per course convention).
     *
     * @param fileName the name (or path) of the file to create/overwrite
     * @param records  the completed CSV-formatted records to write
     */
    private static void writeFile(String fileName, ArrayList<String> records)
    {
        Path file = Paths.get(fileName);

        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8))
        {
            for (String record : records)
            {
                writer.write(record);
                writer.newLine();
            }
            System.out.println("\nSaved " + records.size() + " record(s) to: " + file.toAbsolutePath());
        }
        catch (IOException e)
        {
            System.out.println("Could not write the file: " + e.getMessage());
        }
    }
}
