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
 * Lets the user pick a Product data file with JFileChooser, reads it with
 * BufferedReader, and displays the records in a formatted columnar table.
 * Uses SafeInput to ask whether to open another file.
 *
 * @author Your Name
 * @version 1.0
 */
public class ProductReader
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
            ArrayList<String[]> products = readFile();

            if (products.isEmpty())
            {
                System.out.println("No product records were loaded.");
            }
            else
            {
                displayTable(products);
            }

            again = SafeInput.getYNConfirm(in, "Do you want to open another file?");
        }
    }

    /**
     * Opens a JFileChooser, then reads the chosen file line by line with a
     * BufferedReader, splitting each line into its four fields.
     *
     * @return an ArrayList where each element is one record's fields,
     *         in the order ID, Name, Description, Cost
     */
    private static ArrayList<String[]> readFile()
    {
        ArrayList<String[]> products = new ArrayList<>();

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Open a Product data file");
        chooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
        chooser.setCurrentDirectory(new File("."));

        if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
        {
            System.out.println("No file chosen.");
            return products;
        }

        Path file = chooser.getSelectedFile().toPath();

        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length < 4)
                {
                    System.out.println("Skipping malformed record: " + line);
                    continue;
                }

                for (int i = 0; i < parts.length; i++)
                {
                    parts[i] = parts[i].trim();
                }
                products.add(parts);
            }
        }
        catch (IOException e)
        {
            System.out.println("Could not read the file: " + e.getMessage());
        }

        return products;
    }

    /**
     * Prints the loaded records as a formatted console table. The cost field
     * is parsed and redisplayed with two decimal places.
     * @param products each element is one record's fields: ID, Name, Description, Cost
     */
    private static void displayTable(ArrayList<String[]> products)
    {
        System.out.println();
        System.out.printf("%-8s %-15s %-28s %10s%n", "ID#", "Name", "Description", "Cost");
        System.out.println("=".repeat(65));

        for (String[] p : products)
        {
            double cost = Double.parseDouble(p[3]);
            System.out.printf("%-8s %-15s %-28s %10s%n", p[0], p[1], p[2], String.format("$%.2f", cost));
        }
    }
}
