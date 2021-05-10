package com.example.remotelist.view.screens.list_screen.dialogs.new_shopping_list_dialog

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.remotelist.R
import com.example.remotelist.library.jetpack_compose.Text
import com.example.remotelist.library.jetpack_compose.innerPadding

@Composable
fun NewShoppingItemDialog(
    onClose: () -> Unit,
    dialogController: NewShoppingItemController = viewModel<NewShoppingItemDialogViewModel>()
) =
    Dialog(onDismissRequest = onClose) {
        val context = LocalContext.current

        var maxCount by dialogController.maxCount
        var name by dialogController.name
        var description by dialogController.description

        Column(
            modifier = Modifier
                .background(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colors.onPrimary
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = R.string.new_shopping_item)
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(text = R.string.name) }
            )
            OutlinedTextField(
                value = if (maxCount == null || maxCount?.let { it <= 0 } == true)
                    ""
                else
                    maxCount.toString(),
                onValueChange = { text: String ->
                    maxCount = text.toIntOrNull()?.let {
                        if (it <= 0)
                            null
                        else
                            it
                    }
                },
                label = { Text(text = R.string.count) },
                isError = maxCount == 0,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(text = R.string.description) })

            Divider(modifier = innerPadding)

            OutlinedButton(
                onClick = {
                    dialogController.create(context = context)
                    onClose()
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = ""
                    )
                    Text(text = R.string.create)
                },
            )

        }
    }

interface NewShoppingItemController {
    fun create(context: Context)

    val description: MutableState<String>
    val name: MutableState<String>
    val maxCount: MutableState<Int?>
}
