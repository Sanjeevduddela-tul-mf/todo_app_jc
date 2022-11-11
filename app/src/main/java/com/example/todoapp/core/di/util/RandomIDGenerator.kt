package com.example.todoapp.core.di.util

import java.util.*

object RandomIDGenerator {
    fun generate() = UUID.randomUUID().toString().replace("-", "".uppercase())
}