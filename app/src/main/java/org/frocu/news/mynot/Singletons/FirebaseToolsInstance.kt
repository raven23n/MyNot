package org.frocu.news.mynot.Singletons

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference



object FirebaseToolsInstance {
    lateinit var instanceFirestoreDB: FirebaseFirestore

    fun initialiceFirestoreInstance(){
        instanceFirestoreDB = FirebaseFirestore.getInstance()
    }
}