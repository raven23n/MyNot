package org.frocu.news.mynot.Databases

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import org.frocu.news.mynot.POJO.Newspaper
import org.frocu.news.mynot.Singletons.FirebaseToolsInstance.instanceFirestoreDB
import org.frocu.news.mynot.Singletons.NewspapersList.newspapers

class NewspapersDatabaseFirestore(val section: String)
    : NewspapersDatabase{

    private var newspapersCollectionReference : CollectionReference
    var endOfQuery: Boolean

    init {
        newspapersCollectionReference = instanceFirestoreDB.collection(section)
        endOfQuery = false
    }

    override fun readNewspapers(newspapersListener: NewspapersDatabase.NewspapersListener){
        newspapers.clear()
        newspapersCollectionReference
                .get()
                .addOnCompleteListener(
                        object: OnCompleteListener<QuerySnapshot> {
                            override fun onComplete(task: Task<QuerySnapshot>) {
                                var newspapersTask : ArrayList <Newspaper> = ArrayList()
                                if (task.isSuccessful) {
                                    var stateOk: Long = 0
                                    for (newspapersDocument in task.result.documents) {
                                        var newspaperReceived = Newspaper(
                                                nameNewspaper = newspapersDocument.id,
                                                stateNewspaper = newspapersDocument.data?.getValue("estado") as Long,
                                                imageNewspaper = newspapersDocument.data?.getValue("imagen") as String,
                                                nameClaseParserNewspaper = newspapersDocument.data?.getValue("nombreClaseParser") as String,
                                                urlNewspaper =  newspapersDocument.data?.getValue("url") as String
                                        )
                                        if(newspaperReceived.stateNewspaper.equals(stateOk))
                                            newspapersTask.add(newspaperReceived)
                                    }
                                } else {
                                    Log.d("Recuperar documentos", "Error getting documents: ", task.exception)
                                }
                                if(task.isComplete){
                                    if(newspapersTask.isNotEmpty()) {
                                        newspapers = newspapersTask
                                        newspapersListener.onRespuesta(true)
                                    }else{
                                        newspapersListener.onRespuesta(false)
                                    }
                                }
                            }
                        }
                )
    }


    override fun readCCAANewspapers(country:String,
                                    autonomous_communities: String,
                                    newspapersListener: NewspapersDatabase.NewspapersListener) {
        newspapers.clear()
        var newpapersCollectionReferenceCCAA = newspapersCollectionReference.
                document(country).
                collection(autonomous_communities)
        //Recuperar todos los documentos de una Collection
        newpapersCollectionReferenceCCAA
                .get()
                .addOnCompleteListener(
                        object: OnCompleteListener<QuerySnapshot> {
                            override fun onComplete(task: Task<QuerySnapshot>) {
                                var newspapersTask : ArrayList <Newspaper> = ArrayList()
                                if (task.isSuccessful) {
                                    var stateOk: Long = 0
                                    for (newspapersDocument in task.result.documents) {
                                        var newspaperReceived = Newspaper(
                                                nameNewspaper = newspapersDocument.id,
                                                stateNewspaper = newspapersDocument.data?.getValue("estado") as Long,
                                                imageNewspaper = newspapersDocument.data?.getValue("imagen") as String,
                                                nameClaseParserNewspaper = newspapersDocument.data?.getValue("nombreClaseParser") as String,
                                                urlNewspaper =  newspapersDocument.data?.getValue("url") as String
                                        )
                                        if(newspaperReceived.stateNewspaper.equals(stateOk))
                                            newspapersTask.add(newspaperReceived)
                                    }
                                } else {
                                    Log.d("Recuperar documentos", "Error getting documents: ", task.exception)
                                }
                                if(task.isComplete){
                                    if(newspapersTask.isNotEmpty()) {
                                        newspapers = newspapersTask
                                        newspapersListener.onRespuesta(true)
                                    }else{
                                        newspapersListener.onRespuesta(false)
                                    }
                                }
                            }
                        }
                )
    }
}
