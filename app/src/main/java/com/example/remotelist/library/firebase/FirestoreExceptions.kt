package com.example.remotelist.library.firebase

import android.content.Context
import com.example.remotelist.R.string.*
import com.example.remotelist.library.common.toast

sealed class FirebaseException(message: String = "") : Exception(message) {
    abstract val toastMessage: Int
}

class NoUserException(message: String = "") : FirebaseException(message = message) {
    override val toastMessage
        get() = toast_no_user
}

class UserNotFoundException(message: String = "") : FirebaseException(message = message) {
    override val toastMessage: Int
        get() = toast_user_not_found
}

class DocumentNotFoundException(message: String = "") : FirebaseException(message = message) {
    override val toastMessage: Int
        get() = toast_document_not_found
}

class RegisterFailedException(message: String = "") : FirebaseException(message = message) {
    override val toastMessage: Int
        get() = register_failed
}

class ShoppingListNotFoundException(message: String = "") : FirebaseException(message = message) {
    override val toastMessage: Int
        get() = toast_collection_not_found
}

class LoginNotFoundException(message: String = "") : FirebaseException(message = message) {
    override val toastMessage: Int
        get() = toast_login_not_found
}

class FriendsNotFoundException(message: String = "") : FirebaseException(message = message) {
    override val toastMessage: Int
        get() = toast_friends_not_found
}

class UserDocumentEmptyException(message: String = "") : FirebaseException(message) {
    override val toastMessage: Int
        get() = toast_user_empty
}

class SignInFailedException(message: String = "") : FirebaseException(message = message) {
    override val toastMessage: Int
        get() = toast_sign_in_failed
}

fun Context.toast(firebaseException: FirebaseException) = toast(firebaseException.toastMessage)