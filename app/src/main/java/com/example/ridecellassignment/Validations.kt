package com.example.ridecellassignment

import android.widget.EditText
import java.util.regex.Matcher
import java.util.regex.Pattern

object Validations {
    fun edtValidation(editText: EditText, error: String?): Boolean {
        if (editText.text.toString().isEmpty()) {
            editText.requestFocus()
            editText.error = error
            return false
        } else editText.error = null
        return true
    }

    fun emailValidation(editText: EditText): Boolean {
        if (editText.text.toString().isEmpty()) {
            editText.requestFocus()
            editText.error = "Enter Email Id"
            return false
        } else if (!isEmailValid(editText.text.toString().trim { it <= ' ' })) {
            editText.requestFocus()
            editText.error = "Enter valid email address"
            return false
        } else editText.error = null
        return true
    }

    fun isEmailValid(email: String): Boolean {
        var isValid = false
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val inputStr: CharSequence = email
        val pattern =
            Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(inputStr)
        if (matcher.matches()) {
            isValid = true
        }
        return isValid
    }

    fun passwordValidation(editText: EditText): Boolean {
        if (editText.text.toString().isEmpty()) {
            editText.requestFocus()
            editText.error = "Enter password"
            return false
        } else if (!isValidPassword(editText.text.toString())) {
            editText.requestFocus()
            editText.error =
                "Your password must be at least 5 characters long, contain at least one number, special character and have a mixture of uppercase and lowercase letters."
            return false
        } else editText.error = null
        return true
    }

    fun isValidPassword(password: String?): Boolean {
        //        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        val PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{5,20}\$"
        val pattern: Pattern = Pattern.compile(PASSWORD_PATTERN)
        val matcher: Matcher = pattern.matcher(password)
        return matcher.matches()
    }
}