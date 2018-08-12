package org.frocu.news.mynot.Databases

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings



class SectionDatabaseFirestore :SectionDatabase{

    /*override fun readSections(): ArrayList<NewsSection> {
        var sections :ArrayList<NewsSection> = ArrayList<NewsSection>()
        var sectionDB : FirebaseFirestore = FirebaseFirestore.getInstance()
        var collectionSections : CollectionReference = sectionDB.collection("Secciones")
        var prueba:String = collectionSections.document().id
        Log.i("Prueba firestore doc:","-"+prueba+"-")
        return sections
    }*/
    override fun readSections() {
        val db = FirebaseFirestore.getInstance()
//        val settings = FirebaseFirestoreSettings.Builder()
//                .setTimestampsInSnapshotsEnabled(true)
//                .build()
//        db.setFirestoreSettings(settings)
        var collectionSections : CollectionReference = db.collection("Secciones")
        var prueba:String = collectionSections.document().id
        Log.i("Prueba firestore doc:","-"+prueba+"-")
    }
}