package org.frocu.news.mynot.Databases

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import org.frocu.news.mynot.NewsSection

class SectionDatabaseFirestore :SectionDatabase{

    override fun readSections(): ArrayList<NewsSection> {
        var sections :ArrayList<NewsSection> = ArrayList<NewsSection>()
        var sectionDB : FirebaseFirestore = FirebaseFirestore.getInstance()
        var collectionSections : CollectionReference = sectionDB.collection("Secciones")
        var prueba:String = collectionSections.document().id
        Log.i("Prueba firestore doc:","-"+prueba+"-")
        return sections
    }
}