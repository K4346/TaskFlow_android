package com.example.taskflow.domain.use_cases

import android.util.Patterns
import java.util.regex.Pattern

private const val MIN_PASS_LENGTH = 6
private const val PASS_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$"

class AuthUseCase {
    fun isValidEmail(email: String) =
        email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun isValidPassword(password: String) = password.isNotBlank() &&
            password.length >= MIN_PASS_LENGTH
           // && Pattern.compile(PASS_PATTERN).matcher(password).matches()

    fun isValidPhotoUrl(url: String) =
        url.isNotBlank() && Patterns.WEB_URL.matcher(url).matches()

    fun passwordMatches(password: String, repeatPassword: String) =
        password.isNotBlank() && password == repeatPassword
}