package com.example.remotelist.library.jetpack_compose

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
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
import com.example.remotelist.R
import com.example.remotelist.library.data_structures.Refresher
import com.example.remotelist.library.data_structures.ShopItem
import com.example.remotelist.library.common.ON_REFRESH_REPEAT_COUNT
import kotlinx.coroutines.launch

@Composable
inline fun ShopItemComposable(
    shopItem: ShopItem,
    crossinline deleteShopItem: (ShopItem) -> Unit,
    crossinline onBuy: (ShopItem) -> Unit,
    crossinline onRefresh: () -> Unit
) = Row(verticalAlignment = Alignment.CenterVertically) {

    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Box(contentAlignment = Alignment.Center) {
        if (shopItem.counter.isBinary) {
            if (shopItem.counter.isFull)
                Checkbox(checked = true, onCheckedChange = null)
            else
                Checkbox(
                    checked = false,
                    onCheckedChange = {
                        onBuy(shopItem)
                        coroutineScope.launch {
                            repeat(ON_REFRESH_REPEAT_COUNT) { onRefresh() }
                        }
                    }
                )
        } else {
            if (shopItem.counter.isFull)
                Checkbox(checked = true, onCheckedChange = null)
            else {
                Text(text = shopItem.counter.toString())
            }
        }
    }

    Spacer(modifier = innerPadding)

    Column(verticalArrangement = Arrangement.Center) {
        Text(text = shopItem.name)
        if (shopItem.description.isNotEmpty()) {
            Text(
                text = shopItem.description,
                modifier = Modifier.scrollable(
                    state = scrollState,
                    orientation = Orientation.Horizontal
                ),
                style = MaterialTheme.typography.subtitle1
            )
        }
    }

    if (shopItem.counter.isNotBinary && shopItem.counter.isNotFull)
        IconButton(
            onClick = {
                onBuy(shopItem)
                coroutineScope.launch {
                    repeat(ON_REFRESH_REPEAT_COUNT) { onRefresh() }
                }
            },
        ) {
            Icon(imageVector = Icons.Default.PlusOne, contentDescription = null)
        }

    IconButton(
        onClick = {
            deleteShopItem(shopItem)
            coroutineScope.launch {
                repeat(ON_REFRESH_REPEAT_COUNT) { onRefresh() }
            }
        },
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null
        )
    }
}

@Composable
fun ExtendedFloatingActionButton(
    imageVector: ImageVector,
    text: String,
    onClick: () -> Unit
) = ExtendedFloatingActionButton(
    icon = {
        Icon(
            imageVector = imageVector,
            contentDescription = null
        )
    },
    text = { Text(text) },
    onClick = onClick
)

@Composable
fun RefreshButton(refresher: Refresher) {
    val context = LocalContext.current
    RefreshButton {
        refresher.refresh(context = context)
    }
}

@Composable
fun RefreshButton(onClick: () -> Unit) = IconButton(onClick = onClick) {
    Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
}

@Composable
fun Text(
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
    onTextLayout: (TextLayoutResult) -> Unit = {},
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

@Composable
fun LoginButton(login: String, onClick: () -> Unit) = Login(
    modifier = Modifier
        .clickable(onClick = onClick),
    login = login
)

@Composable
fun AddFriendDialogButton(onClick: () -> Unit) = OutlinedButton(onClick = onClick) {
    Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null)
    Spacer(modifier = innerPadding)
    Text(text = R.string.add_friend, style = MaterialTheme.typography.button)
}

@Composable
fun Login(modifier: Modifier = Modifier, login: String) = Row(modifier = modifier) {
    Icon(
        imageVector = Icons.Default.AccountCircle,
        contentDescription = null
    )
    Spacer(modifier = innerPadding)
    Text(
        text = login,
        style = MaterialTheme.typography.h6
    )
}

@Composable
fun Login(login: String) = Row {
    Icon(
        imageVector = Icons.Default.AccountCircle,
        contentDescription = null
    )
    Spacer(modifier = innerPadding)
    Text(
        text = login,
        style = MaterialTheme.typography.h6
    )
}

@Composable
fun RegisterDialogButton(onClick: () -> Unit) = OutlinedButton(
    onClick = onClick,
    content = {
        Text(
            text = R.string.register,
            style = MaterialTheme.typography.button
        )
    }
)