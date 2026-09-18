package ca.hccis.files.util;

import javax.swing.*;
import java.util.Scanner;

import static ca.hccis.files.util.ValidationUtility.*;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class InputUtility {
    public static final String ERROR_INVALID = "Error: Invalid input. ";
    public static final boolean isGUI = false; // @modified 20260218 BML

    private static final Scanner input = new Scanner(System.in);

    /**
     * Get a String from the user and ensure that it's within an acceptable range for the length.
     * @param message the prompt to display for the user
     * @param isTrimmed whether to trim the user's input or not
     * @param minLength the minimum length of the user's input. -1 if no minimum length is required
     * @param maxLength the maximum length of the user's input. -1 if no maximum length is required
     * @return the user's input
     * @since 20260113
     * @author BML
     * @modified 20260204 BML Changed minLength and maxLength purpose to include the choice to have no min or max length if the value of the parameter is -1.
     * @modified 20260218 BML Added universal isGui functionality.
     */
    public static String getInputString(String message, boolean isTrimmed, int minLength, int maxLength){
        String output, prompt = message;

        do {
            output = getInputString(prompt, isTrimmed);

            // @modified 20260204 BML If minLength or maxLength is -1, there is no effective minimum or maximum String length respectively.
            if (!isValidStringLength(output, minLength, 0) || !isValidStringLength(output, maxLength, 1)) {
                prompt = ERROR_INVALID + message;
            }
        } while (!isValidStringLength(output, minLength, 0) || !isValidStringLength(output, maxLength, 1));

        return output;
    }

    /**
     * A utility method to be used by the main getInputString methods to account for the isGUI setting.
     * @param message the message to display
     * @param isTrimmed true to trim output, false to not trim output
     * @return the user's input
     * @since 20260218
     * @author BML
     */
    private static String getInputString(String message, boolean isTrimmed) {
        String output;

        if (isGUI) {
            output = JOptionPane.showInputDialog(null, message).trim(); // Get the user's input.
        } else {
            System.out.println(message); // Display the message in the console.
            output = input.nextLine(); // Get the user's input.
        }

        return (isTrimmed) ? output.trim() : output;
    }

    /**
     * Method to display a menu to the user and get their selection. Includes validation and will ask the user on a loop until they give a valid selection.
     * @param menu the menu to display to the user
     * @param options a String array of options, including the exit option if applicable.
     * @return the user's selection
     * @since 20260127
     * @author BML
     * @modified BML 20260208 Changed the name of the method from getInputMenu() to getInputStringMenu(). Moved the menu to the do loop so that it will be printed again with each loop.
     * @modified 20260218 BML Added universal isGUI functionality.
     * @modified 20260304 BML Added the option for null options.
     */
    public static String getInputMenu(String menu, String[] options) {
        String selection, prompt = menu;
        boolean isOption = false; // Default to false (not an option)

        do {
            selection = getInputString(prompt, true); // Get the user's input.

            // If no options were provided, just return the user's selection.
            if (options == null) {
                return selection;
            }

            // Check the options array for a matching option.
            for (String option : options) {
                if (option.equalsIgnoreCase(selection)) {
                    isOption = true; // The option was found in the array.
                    break; // Break out of the loop since a match was found.
                }
            }

            // If no match was found, display an error message to the user.
            if (!isOption) {
                prompt = ERROR_INVALID + System.lineSeparator() + menu;
            }
        } while (!isOption);

        return selection;
    }

    /**
     * Method to display a menu to the user and get their selection. Includes validation and will ask the user on a loop until they give a valid selection.
     * @param menu the menu to display to the user
     * @param options an int array of options, including the exit option if applicable.
     * @return the user's selection
     * @since 20260208
     * @author BML
     * @modified 20260218 BML Added isGUI functionality.
     */
    public static int getInputMenu(String menu, int[] options) {
        int selection;
        String prompt = menu;
        boolean isOption = false; // Default to false (not an option)

        do {
            selection = getInputInt(prompt);

            // Check the options array for a matching option.
            for (int option : options) {
                if (option == selection) {
                    isOption = true; // The option was found in the array.
                    break; // Break out of the loop since a match was found.
                }
            }

            // If no match was found, display an error message to the user.
            if (!isOption) {
                prompt = ERROR_INVALID + System.lineSeparator() + menu;
            }
        } while (!isOption);

        return selection;
    }

    /**
     * A utility method to be used by the main getInputInt methods to account for the isGUI setting.
     * @param message the message to display
     * @return the user's input
     * @since 20260218
     * @author BML
     */
    private static int getInputInt(String message) {
        int output;

        if (isGUI) {
            output = parseInt(JOptionPane.showInputDialog(null, message));
        } else {
            System.out.println(message);
            output = input.nextInt();
            input.nextLine(); // Burn the line.
        }

        return output;
    }

    /**
     * Get an int value from the user. Include a minimum and/or maximum value and optionally include the acceptable range in the prompt. Prompts the user on a loop as needed.
     * @param message the prompt to display to the user
     * @param min the minimum accepted value. -1 if no minimum
     * @param max the maximum accepted value. -1 if no maximum
     * @param includeRange true to add the range in the prompt, false to omit the range
     * @return the user's input
     * @since 20260208
     * @author BML
     * @modified 20260218 BML Added isGUI functionality and range validator.
     */
    public static int getInputInt(String message, int min, int max, boolean includeRange) {
        int userInput;
        String prompt = configureIntPrompt(message, min, max, includeRange);
        boolean isValid;

        do {
            userInput = getInputInt(prompt); // Get the user's input.

            isValid = validateIntRange(userInput, min, max); // Validate the user's input.

            if (!isValid) {
                prompt = ERROR_INVALID + System.lineSeparator() + prompt;
            }
        } while (!isValid);

        return userInput;
    }

    /**
     * Set up the prompt for an InputInt operation based on the parameters that are provided.
     * @param message the message to format.
     * @param min the minimum accepted value.
     * @param max the maximum accepted value.
     * @param includeRange whether to include the acceptable range in the prompt.
     * @return the configured string.
     * @author BML
     * @since 2026-03-30
     */
    private static String configureIntPrompt(String message, int min, int max, boolean includeRange) {
        StringBuilder prompt = new StringBuilder(message);

        // If the range should be included in the prompt, include it.
        if (includeRange) {
            String range = " (" + min + "-" + max + ")";

            if (prompt.lastIndexOf(":") != -1) {
                // If a colon was included in the provided message, insert the range in front of it.
                prompt.insert(message.lastIndexOf(":") - 1, range);
            } else {
                // If a colon was not included in the provided message, add it.
                prompt.append(range).append(":");
            }
        } else if (!(message.endsWith(":") || message.endsWith(": "))) {
            // If the message doesn't already have a colon at the end, add one.
            prompt.append(":");
        }

        return prompt.toString();
    }

    /**
     * Get a double value from the user.
     * @param message The message to display as the prompt
     * @return The user's input
     * @since 20251020
     * @author BML
     * @modified 20260218 BML Added isGUI functionality. Switched to private method for use in other public methods.
     */
    private static double getInputDouble(String message){
        double output;

        if (isGUI) {
            output = parseDouble(JOptionPane.showInputDialog(null, message));
        } else {
            System.out.println(message);
            output = input.nextDouble();
            input.nextLine(); // Burn the line.
        }

        return output;
    }

    /**
     * Get a double value from the user. Include a minimum and/or maximum value and optionally include the acceptable range in the prompt. Prompts the user on a loop as needed.
     * @param message the prompt to display to the user
     * @param min the minimum accepted value. -1 if no minimum
     * @param max the maximum accepted value. -1 if no maximum
     * @param includeRange true to add the range in the prompt, false to omit the range
     * @return the user's input
     * @since 20260208
     * @author BML
     */
    public static double getInputDouble(String message, double min, double max, boolean includeRange) {
        StringBuilder prompt = new StringBuilder(message);

        // If the range should be included in the prompt, include it.
        if (includeRange) {
            String range = " (" + min + "-" + max + ")";

            if (prompt.lastIndexOf(":") != -1) {
                // If a colon was included in the provided message, insert the range in front of it.
                prompt.insert(message.lastIndexOf(":") - 1, range);
            } else {
                // If a colon was not included in the provided message, add it.
                prompt.append(range).append(":");
            }
        } else if (!(message.endsWith(":") || message.endsWith(": "))) {
            // If the message doesn't already have a colon at the end, add one.
            prompt.append(":");
        }

        double userInput;
        String finalPrompt = prompt.toString();
        boolean isValid;

        do {
            userInput = getInputDouble(finalPrompt); // Get the user's input.

            isValid = validateDoubleRange(userInput, min, max); // Validate the user's input.

            if (!isValid) {
                finalPrompt = ERROR_INVALID + System.lineSeparator() + prompt;
            }
        } while (!isValid);

        return userInput;
    }

    /**
     * Get a boolean input from the user.
     * @param message The message to display as the prompt
     * @return True if the user's input is equal to Y, false if equal to N.
     * @since 20251020
     * @author BML
     * @modified 20260113 BML Removed dependence on getInputString method.
     * @modified 20260218 BML Added isGUI functionality.
     */
    public static boolean getInputBoolean(String message){
        boolean userInput;

        if (isGUI) {
            userInput = JOptionPane.showConfirmDialog(null, message) == JOptionPane.YES_OPTION; // Get the user's input.
        } else {
            System.out.println(message + " (Y/N): "); // Display the message in the console.
            userInput = input.nextLine().trim().equalsIgnoreCase("Y"); // Get the user's input.
        }

        return userInput; // Defaults to false if anything other than "Y" in the console or "Yes" in JOptionPane.
    }
}
