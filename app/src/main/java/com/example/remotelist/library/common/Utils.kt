package com.example.remotelist.library.common

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import com.example.remotelist.library.firebase.FirebaseException
import com.example.remotelist.library.firebase.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Context.toast(@StringRes message: Int) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

@JvmName("validateString")
inline fun String.validate(onValid: (String) -> Unit = {}, onInvalid: () -> Unit) = if(isEmpty())
    onInvalid()
else
    onValid(this)

@JvmName("validateNullInt")
inline fun Int?.validate(onValid: (Int) -> Unit = {}, onInvalid: () -> Unit) {
    if (this == null || this == 0)
        onInvalid()
    else
        onValid(this)
}


infix fun <E> List<E>.with(element: E): List<E> = toMutableList().apply { add(element) }.toList()

suspend inline fun Context.handleException(
    crossinline block: suspend () -> Unit
) = try {
    block()
} catch (e: FirebaseException){
    toast(e)
}

fun Context.launchAndHandleFirestoreExceptions(
    coroutineScope: CoroutineScope,
    block: suspend () -> Unit
) {
    coroutineScope.launch {
        handleException(block = block)
    }
}