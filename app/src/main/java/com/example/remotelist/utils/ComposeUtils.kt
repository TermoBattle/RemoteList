package com.example.remotelist.utils

import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

/*@Composable
fun IconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    imageVector: ImageVector,
    contentDescription: String?,
) = androidx.compose.material.IconButton(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    interactionSource = interactionSource,
    content = {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription
        )
    }
)

@Composable
fun IconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    imageVector: ImageVector,
    contentDescription: String? = null,
    text: String,
) = androidx.compose.material.IconButton(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    interactionSource = interactionSource,
    content = {
        Row{
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription
            )
            Text(text = text)
        }
    }
)*/

@Composable
inline fun ExtendedFloatingActionButton(
    imageVector: ImageVector,
    text: String,
    noinline onClick: () -> Unit
) = androidx.compose.material.ExtendedFloatingActionButton(
    icon = {
        Icon(
            imageVector = imageVector,
            contentDescription = null
        )
    },
    text = { Text(text) },
    onClick = onClick
)

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
//@ExperimentalUnsignedTypes
//fun Icons.Outlined.FilterN(N: UInt) = when (N) {
//    0u -> Filter
//    1u -> Filter1
//    2u -> Filter2
//    3u -> Filter3
//    4u -> Filter4
//    5u -> Filter5
//    6u -> Filter6
//    7u -> Filter7
//    8u -> Filter8
//    9u -> Filter9
//    else -> Filter9Plus
//}
//@ExperimentalUnsignedTypes
//fun Icons.Rounded.FilterN(N: UInt) = when (N) {
//    0u -> Filter
//    1u -> Filter1
//    2u -> Filter2
//    3u -> Filter3
//    4u -> Filter4
//    5u -> Filter5
//    6u -> Filter6
//    7u -> Filter7
//    8u -> Filter8
//    9u -> Filter9
//    else -> Filter9Plus
//}
//@ExperimentalUnsignedTypes
//fun Icons.TwoTone.FilterN(N: UInt) = when (N) {
//    0u -> Filter
//    1u -> Filter1
//    2u -> Filter2
//    3u -> Filter3
//    4u -> Filter4
//    5u -> Filter5
//    6u -> Filter6
//    7u -> Filter7
//    8u -> Filter8
//    9u -> Filter9
//    else -> Filter9Plus
//}
//@ExperimentalUnsignedTypes
//fun Icons.Sharp.FilterN(N: UInt) = when (N) {
//    0u -> Filter
//    1u -> Filter1
//    2u -> Filter2
//    3u -> Filter3
//    4u -> Filter4
//    5u -> Filter5
//    6u -> Filter6
//    7u -> Filter7
//    8u -> Filter8
//    9u -> Filter9
//    else -> Filter9Plus
//}