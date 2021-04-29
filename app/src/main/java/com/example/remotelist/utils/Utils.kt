package com.example.remotelist.utils

import android.content.Context
import android.util.Log
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

operator fun Context.invoke(block: Context.() -> Unit) = block()
operator fun <T> Context.invoke(block: Context.() -> T):T = block()

fun <E> List<E>.with(element: E): List<E> = toMutableList().apply { add(element) }

/*
fun <E> List<E>.with(element: E):List<E> = toMutableList().apply{
    this += element
}.toList()*/
const val MY_TAG = "DebugMessages"

fun log(message:String){
    Log.d(MY_TAG, message)
}