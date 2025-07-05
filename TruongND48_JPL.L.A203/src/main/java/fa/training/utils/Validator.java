package TruongND48_JPL.L.A203.src.main.java.fa.training.utils;

public class Validator {

    public static boolean isValidPhoneNumber(String phoneNumber, java.util.Collection<String> existingPhoneNumbers) {
        // Check if the phone number is not null, matches the pattern,
        // and does not already exist in the collection of existing phone numbers
        return phoneNumber != null && phoneNumber.matches("^0\\d{9,10}$") && (existingPhoneNumbers == null || !existingPhoneNumbers.contains(phoneNumber));
    }

    public static boolean isValidOrderNumber(String number, java.util.Collection<String> existingOrderNumbers) {
        return number != null && number.length() == 10 && (existingOrderNumbers == null || !existingOrderNumbers.contains(number));
    }


}
