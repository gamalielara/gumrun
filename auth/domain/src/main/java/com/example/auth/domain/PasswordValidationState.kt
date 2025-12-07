package com.example.auth.domain

data class PasswordValidationState(
    val hasMinimumLength: Boolean = false,
    val hasNumber: Boolean = false,
    val hasLowercaseChar: Boolean = false,
    val hasUppercaseChar: Boolean = false,
) {
    val isValidPassword: Boolean
        get() = hasMinimumLength && hasNumber && hasLowercaseChar && hasUppercaseChar
}