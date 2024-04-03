package seedu.address.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import seedu.address.model.person.LastMet;

/**
 * This class is responsible for loading and storing the LastMet Overdue value
 */
public class SetStorage {
    private static int value;
    private static final String PATH = "data/setvalue.txt";

    private SetStorage(int day) {
        value = day;
    }

    public int getValue() {
        return value;
    }

    public static void setData(int days) {
        value = days;
        storeData();
    }

    /**
     * Loads the LastMet Overdue value from setvalue.txt
     * @throws IOException
     */
    public static void loadData() throws IOException {
        File file = new File(PATH);

        file.getParentFile().mkdirs();

        if (!file.exists()) {
            file.createNewFile();
            value = 90;
            storeData();
        } else {
            try {
                checkContents(file);
            } catch (NumberFormatException e) {
                createNewFile();
            }
        }
    }

    /**
     * Stores the LastMet Overdue value into setvalue.txt
     */
    public static void storeData() {
        try {
            FileWriter writer = new FileWriter(PATH);
            String setValue = Integer.toString(value);

            writer.write(setValue);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    /**
     * Reset the setvalue.txt file if value is invalid
     */
    public static void createNewFile() {
        value = 90;
        storeData();
    }

    /**
     * Checks the contents of setvalue.txt
     */
    public static void checkContents(File file) throws IOException {
        Scanner scanner = new Scanner(file);

        if (file.length() == 0) {
            createNewFile();
        } else {
            String input = scanner.nextLine();
            Integer inputConverted = Integer.valueOf(input);

            if (inputConverted instanceof Integer) {
                if (inputConverted < 0) {
                    createNewFile();
                } else {
                    value = inputConverted;
                }
                LastMet.setLastMetDuration(value);
            } else {
                createNewFile();
            }
        }
    }
}
