package com.example.auth.domain

/**
 * Abstraction for domain module.
 * Domain module cannot be dependent on third parties dependencies like PATTERNS.matches (Android OS-builtin func)
 */

interface PatternValidator {
    fun matches(value: String): Boolean
}