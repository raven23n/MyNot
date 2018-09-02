package org.frocu.news.mynot.Databases

import android.text.method.TextKeyListener.clear
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.QuerySnapshot
import org.frocu.news.mynot.POJO.Newspaper
import org.frocu.news.mynot.POJO.Section
import org.frocu.news.mynot.Singletons.FirestoreInstance
import org.frocu.news.mynot.Singletons.NewspapersList
import org.frocu.news.mynot.Singletons.SectionList.sections
import kotlin.text.Typography.section


class SectionDatabaseFirestore :SectionDatabase{

    private var sectionsCollectionReference : CollectionReference
    var endOfQuerySections: Boolean

    init {
        sectionsCollectionReference = FirestoreInstance.instanceFirestoreDB.collection("Secciones")
        endOfQuerySections = false
    }

    override fun readSections(sectionsListener: SectionDatabase.SectionsListener) {
        sections.clear()
        sectionsCollectionReference
                .get()
                .addOnCompleteListener(
                        object: OnCompleteListener<QuerySnapshot> {
                            override fun onComplete(task: Task<QuerySnapshot>) {
                                var sectionTask : ArrayList <Section> = ArrayList()
                                if (task.isSuccessful) {
                                    for (sectionsDocument in task.result.documents) {
                                        var sectionReceived = Section(
                                                sectionName = sectionsDocument.id,
                                                sectionColour = sectionsDocument.data?.getValue("colour") as String
                                        )
                                        sectionTask.add(sectionReceived)
                                    }
                                } else {
                                    Log.d("Recuperar secciones", "Error getting sections: ", task.exception)
                                }
                                if(task.isComplete){
                                    sections = sectionTask
                                    sectionsListener.onRespuesta(true)
                                }
                            }
                        }
                )
    }
}