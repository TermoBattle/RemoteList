package com.example.remotelist.viewmodel

import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remotelist.R
import com.example.remotelist.model.Model
import com.example.remotelist.model.data.ShopItem
import com.example.remotelist.model.data.ShoppingList
import com.example.remotelist.model.data.UserState
import com.example.remotelist.utils.firebase.*
import com.example.remotelist.utils.toUnit
import com.example.remotelist.utils.toast
import com.example.remotelist.utils.validate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val model: Model) :
    ViewModel() {
    val maxCount: MutableState<Int?> = mutableStateOf(null)
    val name = mutableStateOf("")
    val description = mutableStateOf("")

    val newShoppingItemDialog = mutableStateOf(false)
    val chooseUserDialog = mutableStateOf(false)

    private val _shoppingList = mutableStateOf(emptyList<ShopItem>())
    val shoppingList: State<ShoppingList> = _shoppingList

    private val _friends = mutableStateOf(emptyList<String>())
    val friends: State<List<String>> = _friends

    private val _friendsCount = mutableStateOf(0)
    val friendsCount:State<Int> = _friendsCount

    init {
        viewModelScope.launch {
            model.userState.collect { userState: UserState ->
                userState.run {
                    launch{ shoppingList.collect { _shoppingList.value = it } }
                    launch{ friends.collect { _friends.value = it } }
                    launch{ friendsCount.collect { _friendsCount.value = it } }
                }
            }
        }
    }

    fun create(context: Context) {
        name.validate {
            context.toast(R.string.toast_no_name)
            return@validate
        }
        maxCount.validate {
            context.toast(R.string.toast_no_count)
            return@validate
        }

        maxCount as MutableState<Int>

        viewModelScope.launch {
            try {
                model.create(
                    maxCount = maxCount.value,
                    name = name.value,
                    description = description.value
                )
            } catch (e: FirebaseException) {
                context.toastFirebaseException(e)
            } catch (otherException: Exception) {
                context.toast(R.string.toast_unknown_error)
            }
        }
    }

    fun increase(context: Context, shopItem: ShopItem): Unit = viewModelScope.launch {
        try {
            model.update(shopItem)
        } catch (e: FirebaseException) {
            context.toastFirebaseException(e)
        } catch (e: Exception) {
            context.toast(R.string.toast_unknown_error)
        }
    }.toUnit()

    fun delete(context: Context, shopItem: ShopItem) {
        viewModelScope.launch {
            try {
                model.delete(shopItem)
            } catch (e: FirebaseException) {
                context.toastFirebaseException(e)
            } catch (e: Exception) {
                context.toast(R.string.toast_unknown_error)
            }
        }
    }

    fun updateUser(login: String) {
        model.chosenUserLogin.value = login
    }

}