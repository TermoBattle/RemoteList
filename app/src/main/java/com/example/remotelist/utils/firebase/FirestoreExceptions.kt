package com.example.remotelist.utils.firebase

import android.content.Context
import com.example.remotelist.R
import com.example.remotelist.utils.toast

sealed class FirebaseException(message: String = "") : Exception(message){
    abstract val toastMessage:Int
}

class NoUserException(message: String = "") : FirebaseException(message) {
    override val toastMessage
        get() = R.string.toast_no_user
}

class UserNotFoundException(message: String = "") : FirebaseException(message) {
    override val toastMessage: Int
        get() = R.string.toast_user_not_found
}

class DocumentNotFoundException(message: String = "") : FirebaseException(message) {
    override val toastMessage: Int
        get() = R.string.toast_document_not_found
}

class RegisterFailedException(message: String = ""): FirebaseException(message) {
    override val toastMessage: Int
        get() = R.string.register_failed
}

class ShoppingListNotFoundException(message: String = ""): FirebaseException(message) {
    override val toastMessage: Int
        get() = R.string.toast_collection_not_found
}

class LoginNotFoundException(message:String = ""): FirebaseException(message) {
    override val toastMessage: Int
        get() = R.string.toast_login_not_found
}
class FriendsNotFoundException(message: String = ""): FirebaseException(message) {
    override val toastMessage: Int
        get() = R.string.toast_friends_not_found
}

class UserIdNotFoundException(message: String = ""):FirebaseException(message) {
    override val toastMessage: Int
        get() = R.string.toast_user_id_not_found
}

class UserDocumentEmptyException(message: String = ""):FirebaseException(message) {
    override val toastMessage: Int
        get() = R.string.toast_user_empty
}

fun Context.toast(firebaseException: FirebaseException) = toast(firebaseException.toastMessage)