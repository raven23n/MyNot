package org.frocu.news.mynot.Singletons

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference



object FirebaseToolsInstance {
    lateinit var instanceFirestoreDB: FirebaseFirestore
    lateinit var instanceStorageDB: FirebaseStorage
    lateinit var instanceStorageRef: StorageReference

    fun initialiceFirestoreInstance(){
        instanceFirestoreDB = FirebaseFirestore.getInstance()
    }

    fun initialiceStorageInstance(){
        instanceStorageDB = FirebaseStorage.getInstance()
        instanceStorageRef = instanceStorageDB.getReferenceFromUrl("gs://mynot-2b4a3.appspot.com")
    }
}