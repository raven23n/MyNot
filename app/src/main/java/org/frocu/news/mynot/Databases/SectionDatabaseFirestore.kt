package org.frocu.news.mynot.Databases

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import org.frocu.news.mynot.POJO.Section
import org.frocu.news.mynot.Singletons.CCAAList.ccaaList
import org.frocu.news.mynot.Singletons.FirebaseToolsInstance.instanceFirestoreDB
import org.frocu.news.mynot.Singletons.SectionList.sections


class SectionDatabaseFirestore :SectionDatabase{

    private var sectionsCollectionReference : CollectionReference
    var endOfQuerySections: Boolean

    init {
        sectionsCollectionReference = instanceFirestoreDB.collection("Secciones")
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
                                        Log.d("Recuperar secciones", "Section recuperada: -"+sectionsDocument.id+"-", task.exception)
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

    override fun readCCAA(sectionsListener: SectionDatabase.SectionsListener) {
        ccaaList.clear()
        var ccaaCollectionReferenceCCAA = sectionsCollectionReference.
                document("C. Autónomas")
        ccaaCollectionReferenceCCAA
                .get()
                .addOnCompleteListener(
                        object: OnCompleteListener<DocumentSnapshot> {
                            override fun onComplete(task: Task<DocumentSnapshot>) {
                                var ccaaTask : ArrayList <Section> = ArrayList()
                                if (task.isSuccessful) {
                                    var campo = "comunidad"
                                    for (i in 1..18) {
                                        var ccaaSection = task.result.data?.getValue(campo + i.toString()) as String
                                        var sectionReceived = Section(
                                                sectionName = ccaaSection
                                                //sectionColour = sectionsDocument.data?.getValue("colour") as String
                                        )
                                        Log.d("Recuperar secciones", "Section recuperada: -" + sectionReceived.sectionName + "-", task.exception)
                                        ccaaTask.add(sectionReceived)
                                    }
                                } else {
                                    Log.d("Recuperar secciones", "Error getting sections: ", task.exception)
                                }
                                if(task.isComplete){
                                    ccaaList = ccaaTask
                                    sectionsListener.onRespuesta(true)
                                }
                            }
                        }
                )
    }
}