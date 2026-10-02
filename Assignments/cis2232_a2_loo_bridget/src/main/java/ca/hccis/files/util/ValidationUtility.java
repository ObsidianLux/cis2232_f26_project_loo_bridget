package ca.hccis.files.util;

public class ValidationUtility {
    /**
     * Check whether a String's length is [less than or equal to] or [greater than or equal to] a specified limit.
     * @param input the String to check
     * @param lengthLimit the maximum or minimum accepted character count. -1 means no limit
     * @param checkType 0 for minimum length, anything else for maximum length
     * @return true if valid length, false if invalid length
     * @since 20260208
     * @author BML
     */
    public static boolean isValidStringLength(String input, int lengthLimit, int checkType) {
        if (checkType == 0) { // 0 means check the minimum length.
            return lengthLimit == -1 || input.length() >= lengthLimit;
        } else { // Default to check the maximum length.
            return lengthLimit == -1 || input.length() <= checkType;
        }
    }

    /**
     * Validate a Canadian postal code.
     * @param postalCode the postal code to validate
     * @return true if valid, false if invalid
     * @since 20260218
     * @author BML
     */
    public static boolean isValidPostalCode(String postalCode) {
        // Validate the postal code.
        String regex = "[A-Za-z]\\d[A-Za-z] ?\\d[A-Za-z]\\d";
        return postalCode.matches(regex);
    }

    /**
     * Validate a phone number with or without an area code.
     * @param phoneNumber the phone number to validate
     * @param requireAreaCode true to require area code, false to require no area code
     * @return true if valid, false if invalid
     * @since 20260218
     * @author BML
     */
    public static boolean isValidPhoneNumber(String phoneNumber, boolean requireAreaCode) {
        // Validate the phone number.
        String regex = (requireAreaCode) ? "^(\\(\\d{3}\\) ?\\d{3}-\\d{4}|\\d{3}-\\d{3}-\\d{4}|\\d{10})$" : "^(\\d{3}-\\d{4}|\\d{7})$";
        return phoneNumber.matches(regex);
    }

    /**
     * Validate the presence of a first and last name.
     * @param fullName the name to validate
     * @return true if valid, false if invalid
     * @since 20260218
     * @author BML
     */
    public static boolean isValidFullName(String fullName) {
        // Validate the name.
        String regex = "[A-Za-z]+ [A-Za-z]+[A-Za-z ]*";
        return fullName.matches(regex);
    }

    /**
     * Verify whether an int is within an acceptable range when applicable. -1 for min means there's no minimum value and -1 for max means there's no maximum value.
     * @param input the int to validate
     * @param min the minimum accepted value or -1 to set no limit
     * @param max the maximum accepted value or -1 to set no limit
     * @return true if within range, false if out-of-range
     * @since 20260218
     * @author BML
     */
    public static boolean validateIntRange(int input, int min, int max) {
        // False if the user's input is less than the minimum acceptable value and -1 means there's no minimum value OR the user's input is greater than the maximum acceptable value and -1 means there's no maximum value.
        return (min == -1 || input >= min) && (max == -1 || input <= max);
    }

    /**
     * Verify whether a double is within an acceptable range when applicable. -1 for min means there's no minimum value and -1 for max means there's no maximum value.
     * @param input the double to validate
     * @param min the minimum accepted value or -1 to set no limit
     * @param max the maximum accepted value or -1 to set no limit
     * @return true if within range, false if out-of-range
     * @since 20260218
     * @author BML
     */
    public static boolean validateDoubleRange(double input, double min, double max) {
        // False if the user's input is less than the minimum acceptable value and -1 means there's no minimum value OR the user's input is greater than the maximum acceptable value and -1 means there's no maximum value.
        return (min == -1 || input >= min) && (max == -1 || input <= max);
    }
}
