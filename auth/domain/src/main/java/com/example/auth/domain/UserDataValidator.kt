package com.example.auth.domain


class UserDataValidator(private val patternValidator: PatternValidator) {
    fun isValidEmail(email: String): Boolean {
        return patternValidator.matches(email)
    }

    fun validatePassword(password: String): PasswordValidationState {
        val hasMinimumLength = password.length >= MIN_PASSWORD_LENGTH
        val hasNumber = password.any { it.isDigit() }
        val hasUppercaseChar = password.any { it.isUpperCase() }
        val hasLowercaseChar = password.any { it.isLowerCase() }

        return PasswordValidationState(
            hasMinimumLength = hasMinimumLength,
            hasNumber = hasNumber,
            hasUppercaseChar = hasUppercaseChar,
            hasLowercaseChar = hasLowercaseChar
        )
    }


    companion object {
        const val MIN_PASSWORD_LENGTH = 9
    }
}