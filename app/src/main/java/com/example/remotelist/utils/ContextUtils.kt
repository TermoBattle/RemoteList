package com.example.remotelist.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.toast(@StringRes message: Int) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

