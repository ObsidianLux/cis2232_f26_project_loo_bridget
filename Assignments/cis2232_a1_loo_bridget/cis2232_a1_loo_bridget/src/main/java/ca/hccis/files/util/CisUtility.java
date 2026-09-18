package ca.hccis.files.util;

import javax.swing.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import static ca.hccis.files.util.InputUtility.isGUI;

public class CisUtility {
    //https://www.santhoshreddymandadi.com/java/coloring-java-output-on-console.html
    public static final String BLACK = "\033[30m", RED = "\033[31m", GREEN = "\033[32m", YELLOW = "\033[33m";

    public static final boolean DEBUGGING = false;

    /**
     * Provide today's date in the specified format.
     * @param format Desired date format
     * @return Today's date in the specified format
     * @since 20251020
     * @author BML
     * @modified 20260218 BML Added default format.
     */
    public static String getTodayString(String format) {
        // Set a default format if none is provided.
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd";
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        LocalDateTime now = LocalDateTime.now();

        return dtf.format(now);
    }

    /**
     * Get a random number between the specific minimum and maximum numbers.
     * @param min The minimum value
     * @param max The maximum value
     * @return A random number between the specific values
     * @since 20251020
     * @author BML
     */
    public static int getRandom(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max - min + 1) + min;
    }

    /**
     * Method to display a string for the user.
     * @param output The string that will be displayed to the user
     * @since 20181115
     * @author BJM
     * @modified 20260126 BML Added isGUI parameter to the method.
     * @modified 20260218 BML Removed isGUI parameter from the method.
     */
    public static void display(String output) {
        if (isGUI) {
            JOptionPane.showMessageDialog(null, output);
        } else {
            System.out.println(output);
        }
    }

    /**
     * Method to display a string for the user.
     * @param output The string that will be displayed to the user
     * @param color Red, Black (default), Yellow or Green
     * @since 20181115
     * @author BJM
     * @modified 20260126 BML Added isGUI parameter to the method.
     * @modified 20260218 BML Removed isGUI parameter from the method.
     */
    public static void display(String output, String color) {
        String colorCode = BLACK; // Default to black.

        if(color.equalsIgnoreCase("Red")) {
            colorCode = RED;
        } else if (color.equalsIgnoreCase("Green")) {
            colorCode = GREEN;
        } else if (color.equalsIgnoreCase("Yellow")) {
            colorCode = YELLOW;
        }

        output = colorCode + output + BLACK;
        output = output.replaceAll("\n", "\n" + colorCode); // Make sure color shows up on multiple lines.

        if (isGUI) {
            JOptionPane.showMessageDialog(null, output);
        } else {
            System.out.println(output);
        }
    }

    /**
     * Return the number formatted with the given number of decimal places
     * @param theNumber The double number to format.
     * @param numberOfDecimalPlaces The number of decimal places to have in the formatted number.
     * @param rounding 0 for no rounding, 1 for rounding up, -1 for rounding down
     * @return The formatted number
     * @since 20251027
     * @author BML
     * @modified 20260218 BML Changed to use DecimalFormat instead of String.format().
     */
    public static String formatDouble (double theNumber, int numberOfDecimalPlaces, int rounding) {
        DecimalFormat df = new DecimalFormat();

        df.setMaximumFractionDigits(numberOfDecimalPlaces); // Maximum number of decimal places
        df.setMinimumFractionDigits(numberOfDecimalPlaces); // Minimum number of decimal places

        switch (rounding) {
            case 1:
                df.setRoundingMode(RoundingMode.HALF_UP); // Round up
            case -1:
                df.setRoundingMode(RoundingMode.HALF_DOWN); // Round down
            case 0:
                df.setRoundingMode(RoundingMode.UNNECESSARY); // Don't round
        }

        return df.format(theNumber);
    }

    /**
     * Returns a String with the double amount formatted as currency with 2 decimal places and a dollar sign.
     * @param amount The amount as a String to be converted
     * @return The amount as currency
     * @since 20251006
     * @author BML
     */
    public static String toCurrency(double amount) {
        return NumberFormat.getCurrencyInstance().format(amount);
    }

    /**
     * Enforce standard formatting of a postal code.
     * @param postalCode the postal code to format
     * @return the formatted postal code
     * @since 20260218
     * @author BML
     */
    public static String formatPostalCode(String postalCode) {
        // Standardize the format of the postal code.
        StringBuilder formattedPostalCode = new StringBuilder();
        postalCode = postalCode.toUpperCase(); // Turn it all to uppercase.
        if (!postalCode.contains(" ")) {
            formattedPostalCode.append(postalCode, 0, 3).append(" ").append(postalCode.substring(3)); // If there is no space, add one.
        } else {
            formattedPostalCode.append(postalCode);
        }

        return formattedPostalCode.toString();
    }

    /**
     * Format a full name so that each part of the name begins with a capital letter.
     * @param fullName the name to format
     * @return the formatted name
     * @since 20260218
     * @author BML
     */
    public static String formatFullName(String fullName) {
        // Standardize the capitalization.
        String[] names = fullName.split(" ");
        StringBuilder formattedName = new StringBuilder();
        for (String word: names) {
            if (formattedName.isEmpty()) {
                formattedName.append(CisUtility.toUpperCase(word));
            } else {
                formattedName.append(" ").append(CisUtility.toUpperCase(word));
            }
        }

        return formattedName.toString();
    }

    /**
     * Format a string so that the first letter is uppercase and the rest of the string is lowercase.
     * @param input the string to format
     * @return the formatted string
     * @since 20260111
     * @author BML
     * @modified 20260121 BML Fixed the second index for the first substring.
     */
    public static String toUpperCase(String input) {
        return input.trim().substring(0,1).toUpperCase() + input.substring(1);
    }

    /**
     * Dynamically build a menu String.
     * @param includeBorder true to include a line of dashes at the top and bottom of the menu, false to omit the dashes
     * @param menuLines an array of Strings where each is one line in the menu
     * @param menuHeader the header to include at the top of the menu. Provide "" to omit a header
     * @return the menu String
     * @author BML
     * @since 2026-03-11
     */
    public static String menuBuilder(boolean includeBorder, String[] menuLines, String menuHeader) {
        StringBuilder menu = new StringBuilder();
        if (includeBorder) menu.append("--------------------").append(System.lineSeparator());
        if (!menuHeader.isEmpty()) menu.append(menuHeader).append(System.lineSeparator());
        for (String line: menuLines) menu.append(line).append(System.lineSeparator());
        if (includeBorder) menu.append("--------------------").append(System.lineSeparator());
        return menu.toString();
    }
}
