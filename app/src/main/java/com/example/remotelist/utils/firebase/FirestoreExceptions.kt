package com.example.remotelist.utils.firebase

import android.content.Context
import com.example.remotelist.R
import com.example.remotelist.utils.toast

sealed class FirebaseException(message: String = "") : Exception(message)
class NoUserException(message: String = "") : FirebaseException(message)
class UserNotFoundException(message: String = "") : FirebaseException(message)
class DocumentNotFoundException(message: String = "") : FirebaseException(message)
class CannotUpdateUser(message: String = "") : FirebaseException(message)
class NoShoppingList(message: String = "") : FirebaseException(message)
class NoFriendsListException(message: String = ""):FirebaseException(message)


fun Context.toastFirebaseException(firebaseException: FirebaseException) = toast(
    when (firebaseException) {
        is NoUserException -> R.string.toast_no_user
        is UserNotFoundException -> R.string.toast_user_not_found
        is DocumentNotFoundException -> R.string.toast_item_not_found
        is CannotUpdateUser -> R.string.toast_cannot_update_user
        is NoShoppingList -> R.string.toast_shopping_list_not_found
        is NoFriendsListException -> R.string.toast_no_friends
    }
)