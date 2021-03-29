package com.example.remotelist.model

import com.example.remotelist.model.data.ShopItem
import com.example.remotelist.model.data.UserState
import com.example.remotelist.model.repository.UserRepository
import com.example.remotelist.utils.firebase.DocumentNotFoundException
import com.example.remotelist.utils.firebase.getSnapshot
import com.example.remotelist.utils.toUnit
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class Model @Inject constructor(private val userRepo: UserRepository) {
    val chosenUserLogin: MutableStateFlow<String> = MutableStateFlow("")

    private val _userState: MutableStateFlow<UserState> = MutableStateFlow(UserState())
    val userState = _userState.asStateFlow()

    init {
        GlobalScope.launch {
            chosenUserLogin.collect { login ->
                if(login.isBlank())
                    _userState.emit(userRepo.UserState(login = userRepo.currentUserLogin.value))
                else
                    _userState.emit(userRepo.UserState(login))
            }
        }
    }

    suspend fun update(shopItem: ShopItem) = userRepo.run {
        if (chosenUserLogin.value.isBlank())
            getCurrentUserDocument()
        else
            getUserDocument(login = chosenUserLogin.value)
    }.shoppingList
        .findDocument(shopItem.name)
        ?.set(shopItem.increased())
        ?.await()
        ?: throw DocumentNotFoundException()

    suspend fun create(maxCount: Int, name: String, description: String) = userRepo
        .getCurrentUserDocument()
        .shoppingList
        .add(ShopItem(maxCount, name, description))
        .toUnit()

    suspend fun delete(shopItem: ShopItem) {
        userRepo
            .getCurrentUserDocument()
            .shoppingList
            .findDocument(shopItem.name)
            ?.delete()
            ?: throw DocumentNotFoundException()
    }

}

private suspend fun CollectionReference.findDocument(name: String): DocumentReference? = this
    .whereEqualTo(NAME, name)
    .getSnapshot()
    ?.first()
    ?.reference

private val DocumentReference.shoppingList: CollectionReference
    inline get() = collection(SHOPPING_LIST)