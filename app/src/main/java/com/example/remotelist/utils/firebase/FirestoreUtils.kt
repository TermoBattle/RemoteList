package com.example.remotelist.utils.firebase

import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await

suspend inline fun Query.getSnapshot():QuerySnapshot? = get().await()

