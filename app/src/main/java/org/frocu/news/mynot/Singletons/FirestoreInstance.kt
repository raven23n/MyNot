package org.frocu.news.mynot.Singletons

import com.google.firebase.firestore.FirebaseFirestore

object FirestoreInstance {
    lateinit var instanceFirestoreDB: FirebaseFirestore

    fun initialiceFirestoreInstance(){
        instanceFirestoreDB = FirebaseFirestore.getInstance()
    }
}