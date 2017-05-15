package com.example.android.roomrent.Helper;

import android.app.Application;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import com.example.android.roomrent.Activity.R;
import com.example.android.roomrent.Activity.RoomRentApplication;

import java.util.regex.Pattern;

import static com.example.android.roomrent.Activity.RoomRentApplication.getTextInputLayout;

public class InputValidatorHelper extends Application  {

    /** Tag for the log messages */
    public static final String LOG_TAG = InputValidatorHelper.class.getSimpleName();
    // Regular Expression
    // you can change the expression based on your need
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)" +
            "*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_REGEX = "(\\+977- ?|\\+977 ?|0)?[9]?[87]\\d{8}";
    private static final String PASSWORD_REGEX = "(?=(.*[a-z])+)(?=(.*[0-9])+)" +
            "[0-9a-zA-Z!\\\"#$%&'()*+,\\-.\\/:;<=>?@\\[\\\\\\]^_`{|}~]{8,15}";
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9_.]*$";

    private TextInputLayout textInputLayout;
    // private constructor so that it cannot be instantiated directly
    private  InputValidatorHelper(){}

    // method to get only object
    public static InputValidatorHelper getInstance(){
        return SingletonHelper.INSTANCE;
    }

    /**
     * Input validation goes here
     * <p>
     * EditText whose values should be checked for error
     * such as matches for regrex, empty fields, required min character can be processed
     * <p>
     * sets error on parent TextInputLayout if EditText is enclosed with it
     * else sets error on the edit text itself     *
     * Finally, shaking animation for 1 second in case EditText has errors     *
     */
    public void compareWithRegex(EditText... editTexts) {
        for (final EditText editText : editTexts) {
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus && !checkValidation(editText, true)) {
                        editText.startAnimation(RoomRentApplication.animation);
                    }
                }
            });
        }
    }

    public boolean checkValidation(EditText editText, boolean required) {
        String errMsg, regex;
        int minimum;
        boolean validFlag;


        switch (editText.getInputType()) {

            case 33: // if it is email
                regex = EMAIL_REGEX;
                errMsg = RoomRentApplication.getContext().getString(R.string.validator_email);
                minimum = Integer.parseInt(RoomRentApplication.getContext().
                        getString(R.string.validator_minimium_email_character));
                validFlag = isValid(editText, regex, errMsg, minimum, required);
                break;

            case 97: // if it is username
                regex = USERNAME_REGEX;
                errMsg = RoomRentApplication.getContext().getString(R.string.validator_username);
                minimum = Integer.parseInt(RoomRentApplication.getContext().
                        getString(R.string.validator_minimium_user_name_character));
                validFlag = isValid(editText, regex, errMsg, minimum, required);
                break;

            case 2: // if it is phone
                regex = PHONE_REGEX;
                errMsg = RoomRentApplication.getContext().getString(R.string.validator_phone);
                minimum = Integer.parseInt(RoomRentApplication.getContext().
                        getString(R.string.validator_minimium_phone_character));

                validFlag = isValid(editText, regex, errMsg, minimum, required);
                break;

            case 129: // if it is password
                regex = PASSWORD_REGEX;
                errMsg = RoomRentApplication.getContext().getString(R.string.validator_pass);
                minimum = Integer.parseInt(RoomRentApplication.getContext().
                        getString(R.string.validator_minimium_password_character));
                validFlag = isValid(editText, regex, errMsg, minimum, required);
                break;

            default:
                validFlag = hasText(editText);
                break;

        }
        return validFlag;

    }

    // return true if the input field is valid, based on the parameter passed
    public boolean isValid(EditText editText, String regex, String errMsg, int minimum,
                           boolean required) {
        textInputLayout = getTextInputLayout(editText);

        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        if (textInputLayout == null) {
            editText.setError(null);
        } else {
            textInputLayout.setError(null);
        }

        // text required and editText is blank, so return false
        if (required && !hasText(editText)) return false;

        // text required doesnot has sufficient characters, so return false
        //  if (required && !minimumText(editText, minimum)) return false;

        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            if (textInputLayout == null) {
                editText.setError(errMsg);
            } else {
                textInputLayout.setError(errMsg);
            }
            return false;
        }

        return true;
    }

    // check the input field has any text or not
    // return true if it contains text otherwise false
    public boolean hasText(EditText editText) {
        String text = editText.getText().toString().trim();
        textInputLayout = getTextInputLayout(editText);


        if (textInputLayout == null) {
            editText.setError(null);
        } else {
            textInputLayout.setError(null);
        }

        // length 0 means there is no text
        if (text.length() == 0) {
            if (textInputLayout == null) {
                editText.setError(RoomRentApplication.getContext().
                        getString(R.string.validator_empty));
            } else {
                textInputLayout.setError(RoomRentApplication.getContext().
                        getString(R.string.validator_empty));
            }
            return false;
        }

        return true;
    }

    // check the input field has any specified nimminum of text or not
    // return true if it contains sufficient text otherwise false
    public boolean minimumText(EditText editText, int min) {
        String text = editText.getText().toString().trim();
        textInputLayout = getTextInputLayout(editText);


        if (textInputLayout == null) {
            editText.setError(null);
        } else {
            textInputLayout.setError(null);
        }
        // length 0 means there is no text
        if (text.length() < min) {
            if (textInputLayout == null) {
                editText.setError(RoomRentApplication.getContext().
                        getString(R.string.validator_minimum) + "  " + min);
            } else {
                textInputLayout.setError(RoomRentApplication.getContext().
                        getString(R.string.validator_minimum) + "  " + min);
            }
            return false;
        }

        return true;
    }

    // check if input password matches with confirm password or not
    // return true if yes else return false
    public boolean confirmPassword(EditText password, EditText confirmPassword) {
        String pass = password.getText().toString().trim();
        String confirmPass= confirmPassword.getText().toString().trim();

        textInputLayout = getTextInputLayout(confirmPassword);

        if (textInputLayout == null) {
            confirmPassword.setError(null);
        } else {
            textInputLayout.setError(null);
        }

        // length 0 means there is no text
        if (!(pass.equals(confirmPass))) {
            if (textInputLayout == null) {
                confirmPassword.setError(RoomRentApplication.getContext().
                        getString(R.string.validator_confirm));
            } else {
                textInputLayout.setError(RoomRentApplication.getContext().
                        getString(R.string.validator_confirm));
            }
            return false;
        }
        return true;
    }

    //  return single object
    private static class SingletonHelper {
        private static InputValidatorHelper INSTANCE = new InputValidatorHelper();
    }

}