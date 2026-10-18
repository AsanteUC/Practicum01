import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Prompts the user for Person records (ID, FirstName, LastName, Title,
 * YearOfBirth), stores each completed record in an ArrayList, and writes the
 * whole list to a comma-delimited text file using BufferedWriter.
 * <p>
 * File format, one record per line, no header:
 * ID, FirstName, LastName, Title, YearOfBirth
 *
 * @author Your Name
 * @version 1.0
 */
public class PersonGenerator
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

        System.out.println("===== Person Generator =====");

        while (moreData)
        {
            String id = SafeInput.getRegExString(in, "Enter the 6 digit ID", "\\d{6}");
            String firstName = SafeInput.getNonZeroLenString(in, "Enter the first name");
            String lastName = SafeInput.getNonZeroLenString(in, "Enter the last name");
            String title = SafeInput.getNonZeroLenString(in, "Enter the title [Mr. Mrs. Ms. Dr. Esq. etc.]");
            int yob = SafeInput.getInt(in, "Enter the year of birth");

            // Only a fully collected record ever reaches the ArrayList
            String record = id + ", " + firstName + ", " + lastName + ", " + title + ", " + yob;
            records.add(record);

            System.out.println("\nAdded record: " + record);
            moreData = SafeInput.getYNConfirm(in, "Do you want to enter another person?");
        }

        String fileName = SafeInput.getNonZeroLenString(in, "Enter the file name to save to, e.g. PersonTestData.txt");
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
