package ca.hccis.files.util;

import javax.swing.*;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class GameUtility {
    public static final String ERROR_INVALID = "Error: Invalid input. ";
    public static final String CANCEL = "X";

    private static final Scanner input = new Scanner(System.in);

    public boolean isGUI = false;

    public GameUtility() {}

    public GameUtility(boolean isGUI) {
        this.isGUI = isGUI;
    }

    /**
     * Method to display a string for the user.
     * @param output The string that will be displayed to the user
     * @since 20181115
     * @author BJM
     * @modified 20260126 BML Added isGUI parameter to the method.
     * @modified 20260218 BML Removed isGUI parameter from the method.
     */
    public void display(String output) {
        if (isGUI) {
            JOptionPane.showMessageDialog(null, output);
        } else {
            System.out.println(output);
        }
    }

    /**
     * Method to get user string input.
     * @param message The message to display to the user.
     * @param isRequired True if a value is required, false otherwise.
     * @return The user's input (trimmed) or the CANCEL string if they canceled in JOptionPane.
     * @author BML
     * @since 20261012
     */
    public String getInputString(String message, boolean isRequired) {
        String output, prompt = message;

        // Loop until the user input is valid.
        boolean isValid;
        do {
            isValid = true;

            if (isGUI) {
                output = JOptionPane.showInputDialog(null, prompt);
            } else {
                System.out.println(prompt); // Display the prompt in the console.
                output = input.nextLine(); // Get the user's input.
            }

            // If the output is null, the user clicked cancel.
            if (output == null) return CANCEL;
            else output = output.trim();

            // Make sure the output isn't an empty string.
            if (isRequired && output.isEmpty()) {
                prompt = ERROR_INVALID + System.lineSeparator() + message;
                isValid = false;
            }

        } while (!isValid);

        return output;
    }

    /**
     * Get an int value from the user. Prompts the user on a loop as needed. Minimum value of 0.
     * @param message the prompt to display to the user
     * @return the user's input or -1 if the user clicked Cancel
     * @since 20261012
     * @author BML
     */
    public int getInputInt(String message) {
        int output = 0; // Default to 0.
        String prompt = message;

        boolean isValid;

        do {
            isValid = true;

            try {
                if (isGUI) {
                    String userInput = JOptionPane.showInputDialog(null, prompt);

                    // If the user clicks Cancel, the dialog returns null. -1 will indicate to the caller that the operation was canceled.
                    if (userInput == null) output = -1;
                    else output = parseInt(userInput);
                } else {
                    System.out.println(prompt);
                    output = input.nextInt();
                    input.nextLine(); // Burn the line.
                }
            // Catch NumberFormatException and set up to loop again.
            } catch (NumberFormatException | InputMismatchException e) {
                prompt = ERROR_INVALID + System.lineSeparator() + message;
                isValid = false;
            }
        } while (!isValid);

        return output;
    }

    /**
     * Get a boolean input from the user.
     * @param message The message to display as the prompt. Don't include " (Y/N):" in the prompt as it will be added automatically.
     * @return True if the user's input is equal to Y, false if equal to N. Empty Optional if the user canceled in JOptionPane.
     * @since 20261012
     * @author BML
     */
    public Optional<Boolean> getInputBoolean(String message) {
        Optional<Boolean> output;

        if (isGUI) {
            int userInput = JOptionPane.showConfirmDialog(null, message); // Get the user's input.

            if (userInput == JOptionPane.YES_OPTION) output = Optional.of(true);
            else if (userInput == JOptionPane.NO_OPTION) output = Optional.of(false);
            else output = Optional.empty();

        } else {
            System.out.println(message + " (Y/N): "); // Display the message in the console.
            output = Optional.of(input.nextLine().trim().equalsIgnoreCase("Y")); // Get the user's input.
            // Defaults to false if anything other than "Y" in the console.
        }

        return output;
    }
}
