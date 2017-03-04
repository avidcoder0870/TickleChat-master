package com.techpro.chat.ticklechat.utils;

import android.util.Patterns;

import java.util.List;
import java.util.Set;

import retrofit.Response;

public class ValidationUtils {

    public static final String REGEX_LANDLINE_NUMBER = "^[+]?[0-9]{10,13}$";

    public static boolean validateEmailAddress(String email) {
        return validateObject(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean validatePassword(String password) {
        return validateObject(password) && password.trim().length() >= 4;
    }

    public static boolean validatePhoneNumber(String phone) {
        return validateObject(phone) && Patterns.PHONE.matcher(phone).matches() && phone.trim().length() == 10;
    }

    public static boolean validateObject(Object object) {
        if (object == null || object.equals(null))
            return false;
        if (object instanceof String && ((String) object).trim().length() <= 0)
            return false;
        else if (object instanceof List && ((List) object).size() <= 0)
            return false;
        else if (object instanceof Set && ((Set) object).size() <= 0)
            return false;
        return true;
    }

    public static boolean validatePasswordConfirmation(String password, String confirmPassword) {
        return validateObject(password) && validateObject(confirmPassword) && password.equals(confirmPassword);
    }

    public static boolean validateRetrofitResponse(Response<?> response) {
        return response.isSuccess() && response.code() == 200 && validateObject(response.body());
    }

    public static boolean validateToken(String token) {
        return validateObject(token) && token.length() > 7;
    }

    public static boolean validatePincode(String pincode, String pinCodeInitials) {
        //if no pin code is added let it pass
        if (!validateObject(pincode)) {

            return true;
        }

        try {
            //just checking if the pin code is in integer format
            Integer.parseInt(pincode);
        } catch (NumberFormatException e) {

            return false;
        }

        String[] pin = pinCodeInitials.split(",");
        boolean isPinCodeValid = false;
        for (String aPin : pin) {
            isPinCodeValid = isPinCodeValid || pincode.startsWith(aPin);
        }

        return pincode.length() == 6 && isPinCodeValid;
    }

    public static boolean validateResponse(String response) {
        return response != null && response.length() >= 50;
    }

    public static boolean validateLandlineNumber(String number) {
        //if no number is added let it pass
        if (!validateObject(number)) {

            return false;
        }
        return number.length() == 11;
    }
}
