package com.sutd.t4app.utility;

public class FormValidation {
    private static final String passregex = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$";
    private static final String emailregex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";


    public static String validatePass(String pass){
        if (pass.matches(passregex)) {
            return null;
        } else {
            return "Password is WEAK";
        }
    }

    public static String validateEmail(String email){
        if (email.matches(emailregex)) {
            return null;
        } else {
            return "Email INVALID";
        }
    }

    public static String validateFirstName(String firstname){
        if (!firstname.isEmpty()) {
            return null;
        } else {
            return "First Name Required";
        }
    }

    public static String validateLastName(String lastname){
        if (!lastname.isEmpty()) {
            return null;
        } else {
            return "Last Name Required";
        }
    }

}
