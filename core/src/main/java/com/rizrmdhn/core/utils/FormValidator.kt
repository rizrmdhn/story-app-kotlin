package com.rizrmdhn.core.utils

object FormValidator {
    fun isNameValid(name: String): Boolean {
        return name.isNotEmpty() && name.length >= 3 && name.matches(Regex("^[a-zA-Z ]+\$"))
    }

    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotEmpty()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length >= 6 && password.isNotEmpty()
    }
}