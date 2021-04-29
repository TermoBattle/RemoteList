package com.example.remotelist.utils

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.remotelist.R
import com.example.remotelist.mvvm.viewmodel.AccountViewModel
import com.example.remotelist.mvvm.viewmodel.ListViewModel

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

@Composable
fun RefreshButton(viewModel: AccountViewModel) = RefreshButton(onClick = viewModel::refresh)

@Composable
fun RefreshButton(viewModel: ListViewModel) = RefreshButton(onClick = viewModel::refresh)

@Composable
fun RefreshButton(onClick: () -> Unit) = OutlinedButton(onClick = onClick) {
    Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
    Spacer(modifier = Modifier.padding(4.dp))
    Text(text = stringResource(R.string.refresh))
}

@Composable
inline fun Text(
    @StringRes text: Int,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    noinline onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) = Text(
    text = stringResource(id = text),
    modifier = modifier,
    color = color,
    fontSize = fontSize,
    fontStyle = fontStyle,
    fontWeight = fontWeight,
    fontFamily = fontFamily,
    letterSpacing = letterSpacing,
    textDecoration = textDecoration,
    textAlign = textAlign,
    lineHeight = lineHeight,
    overflow = overflow,
    softWrap = softWrap,
    maxLines = maxLines,
    onTextLayout = onTextLayout,
    style = style
)