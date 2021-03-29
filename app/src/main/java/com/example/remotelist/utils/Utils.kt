package com.example.remotelist.utils

import androidx.compose.runtime.State

@JvmName("validateString")
inline fun State<String>.validate(onValid: (String) -> Unit = {}, onInvalid: () -> Unit) = if(value.isEmpty())
    onInvalid()
else
    onValid(value)

@JvmName("validateNullInt")
inline fun State<Int?>.validate(onValid: (Int) -> Unit = {}, onInvalid: () -> Unit) = if (value == null || value == 0)
    onInvalid()
else
    onValid(value!!)

fun <T> T.toUnit() = Unit