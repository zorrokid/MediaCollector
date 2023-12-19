package com.zorrokid.mediacollector.common.ext

import android.util.Patterns

fun String.isValidEmail(): Boolean {
    return this.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.capitalizeWords(): String {
    return this
        .split(" ")
        .joinToString(" ") { it.lowercase().replaceFirstChar(Char::titlecaseChar) }
}