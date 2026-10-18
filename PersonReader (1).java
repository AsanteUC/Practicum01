import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Lets the user pick a Person data file with JFileChooser, reads it with
 * BufferedReader, and displays the records in a formatted columnar table.
 * Uses SafeInput to ask whether to open another file.
 *
 * @author Your Name
 * @version 1.0
 */
public class PersonReader
{
    /**
     * Program entry point.
     * @param args command line arguments, unused
     */
    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        boolean again = true;

        while (again)
        {
            ArrayList<String[]> people = readFile();

            if (people.isEmpty())
            {
                System.out.println("No person records were loaded.");
            }
            else
            {
                displayTable(people);
            }

            again = SafeInput.getYNConfirm(in, "Do you want to open another file?");
        }
    }

    /**
     * Opens a JFileChooser, then reads the chosen file line by line with a
     * BufferedReader, splitting each line into its five fields.
     *
     * @return an ArrayList where each element is one record's fields,
     *         in the order ID, FirstName, LastName, Title, YOB
     */
    private static ArrayList<String[]> readFile()
    {
        ArrayList<String[]> people = new ArrayList<>();

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Open a Person data file");
        chooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
        chooser.setCurrentDirectory(new File("."));

        if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
        {
            System.out.println("No file chosen.");
            return people;
        }

        Path file = chooser.getSelectedFile().toPath();

        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length < 5)
                {
                    System.out.println("Skipping malformed record: " + line);
                    continue;
                }

                for (int i = 0; i < parts.length; i++)
                {
                    parts[i] = parts[i].trim();
                }
                people.add(parts);
            }
        }
        catch (IOException e)
        {
            System.out.println("Could not read the file: " + e.getMessage());
        }

        return people;
    }

    /**
     * Prints the loaded records as a formatted console table.
     * @param people each element is one record's fields: ID, First, Last, Title, YOB
     */
    private static void displayTable(ArrayList<String[]> people)
    {
        System.out.println();
        System.out.printf("%-8s %-12s %-12s %-8s %-6s%n", "ID#", "Firstname", "Lastname", "Title", "YOB");
        System.out.println("=".repeat(50));

        for (String[] p : people)
        {
            System.out.printf("%-8s %-12s %-12s %-8s %-6s%n", p[0], p[1], p[2], p[3], p[4]);
        }
    }
}
