package com.example.remotelist.utils.firebase

import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await

suspend inline fun Query.getSnapshot(): QuerySnapshot? = get().await()

suspend inline fun DocumentReference.getSnapshot(): DocumentSnapshot = get().await()

suspend fun CollectionReference.findDocument(name: String): DocumentReference? = this
    .whereEqualTo(NAME, name)
    .getSnapshot()
    ?.first()
    ?.reference

