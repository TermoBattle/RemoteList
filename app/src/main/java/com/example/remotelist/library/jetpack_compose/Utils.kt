package com.example.remotelist.library.jetpack_compose

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource


fun Icons.Filled.FilterN(n: Int) = when (n) {
    0 -> Filter
    1 -> Filter1
    2 -> Filter2
    3 -> Filter3
    4 -> Filter4
    5 -> Filter5
    6 -> Filter6
    7 -> Filter7
    8 -> Filter8
    9 -> Filter9
    else -> Filter9Plus
}

@Composable
operator fun @receiver:StringRes Int.plus(text: String): String = stringResource(id = this) + text

