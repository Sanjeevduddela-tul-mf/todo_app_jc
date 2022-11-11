package com.example.todoapp.core.di.util

object InputValidator {
    fun trim(text: String): String = text.trim()
    fun isValidTitle(text: String) = trim(text).isNotBlank()
    fun isValidDes(text: String) = trim(text).isNotBlank()
    fun isValidEntry(title: String, des: String) = isValidTitle(title) && isValidDes(des)
}