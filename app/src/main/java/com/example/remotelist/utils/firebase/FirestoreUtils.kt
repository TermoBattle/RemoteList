package com.example.remotelist.utils.firebase

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

suspend inline fun Query.getSnapshot():QuerySnapshot? = get().await()

suspend inline fun DocumentReference.getSnapshot():DocumentSnapshot = get().await()