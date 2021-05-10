package com.example.remotelist.view.screens.list_screen.dialogs.new_shopping_list_dialog

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remotelist.R
import com.example.remotelist.library.common.launchAndHandleFirestoreExceptions
import com.example.remotelist.library.common.toast
import com.example.remotelist.library.common.validate
import com.example.remotelist.repositories.FirestoreDataInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewShoppingItemDialogViewModel @Inject constructor() :
    ViewModel(), NewShoppingItemController {

    override val description: MutableState<String> = mutableStateOf("")
    override val name: MutableState<String> = mutableStateOf("")
    override val maxCount: MutableState<Int?> = mutableStateOf(null)


    override fun create(context: Context) {
        val name: String = name.value
        val description = description.value
        val maxCount = maxCount.value

        name.validate(
            onInvalid = {
                context.toast(R.string.toast_no_name)
                return@validate
            },
        )
        maxCount.validate(
            onInvalid = {
                context.toast(R.string.toast_no_count)
                return@validate
            },
        )

        maxCount as Int

        context.launchAndHandleFirestoreExceptions(coroutineScope = viewModelScope) {
            FirestoreDataInteractor.createItem(
                maxCount = maxCount,
                name = name,
                description = description
            )
        }
    }
}