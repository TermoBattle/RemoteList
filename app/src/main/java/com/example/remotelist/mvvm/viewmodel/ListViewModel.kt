package com.example.remotelist.mvvm.viewmodel

import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remotelist.R
import com.example.remotelist.mvvm.model.Model
import com.example.remotelist.mvvm.model.data.ShopItem
import com.example.remotelist.utils.firebase.*
import com.example.remotelist.utils.log
import com.example.remotelist.utils.toast
import com.example.remotelist.utils.validate
import com.google.firebase.firestore.ktx.toObjects
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val model: Model, private val context: Context) :
    ViewModel() {
    val maxCount = mutableStateOf<Int?>(null)
    val name = mutableStateOf("")
    val description = mutableStateOf("")

    val newShoppingItemDialog = mutableStateOf(false)
    val chooseFriendDialog = mutableStateOf(false)

    private val _shoppingList = MutableStateFlow(emptyList<ShopItem>())
    val shoppingList = _shoppingList.asStateFlow()

    private val _friends = MutableStateFlow(emptyList<String>())
    val friends = _friends.asStateFlow()

    private val _friendsCount = MutableStateFlow(0)
    val friendsCount = _friendsCount.asStateFlow()

    init {
        viewModelScope.launch {
            model.userData.collect { user ->

                log("New userData collected")

                if (user != null) {
                    launch { _shoppingList.emit(value = user.shoppingList) }
                    launch { _friends.emit(value = user.friends) }
                    launch { _friendsCount.emit(value = user.friendsCount) }
                }
            }
        }
    }

    private fun toastFirebaseException(e: FirebaseException) = context.toast(e)

    fun create() {
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
                model.createItem(
                    maxCount = maxCount.value,
                    name = name.value,
                    description = description.value
                )
            } catch (e: FirebaseException) {
                toastFirebaseException(e)
            }
        }
    }

    fun increase(shopItem: ShopItem): Unit {
        viewModelScope.launch {
            try {
                model.updateItem(shopItem)
            } catch (e: DocumentNotFoundException) {
            } catch (e: FirebaseException) {
                toastFirebaseException(e)
            }
        }
    }

    fun delete(shopItem: ShopItem) {
        viewModelScope.launch {
            try {
                model.deleteItem(shopItem)
            } catch (e: FirebaseException) {
                toastFirebaseException(e)
            }
        }
    }

    fun updateUser(login: String) {
        viewModelScope.launch { model.chosenUserLogin.emit(login) }
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                model.getShoppingList().getSnapshot()?.let { _shoppingList.emit(it.toObjects()) }
            } catch (e: FirebaseException) {
                toastFirebaseException(e)
            }
            try {
                _friends.emit(
                    model.getCurrentFriendList()
                        .also {
                            _friendsCount.emit(it.size)
                        }
                )
            } catch (e: FirebaseException) {
                toastFirebaseException(e)
            }

        }
    }

}