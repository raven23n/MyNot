package org.frocu.news.mynot.Databases

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import org.frocu.news.mynot.POJO.Newspaper
import org.frocu.news.mynot.POJO.newspaperEmpty
import org.jetbrains.annotations.NotNull

class NewspapersDatabaseFirestore(val section: String)
    : NewspapersDatabase{

    private lateinit var newpapersCollectionReference : CollectionReference

    init {
        var sectionDB:FirebaseFirestore = FirebaseFirestore.getInstance()
        newpapersCollectionReference = sectionDB.collection(section)
    }

    override fun readNewspapers(newspapersListener: NewspapersDatabase.NewspapersListener) {
        var newspapers : ArrayList <Newspaper> = ArrayList()
        //Recuperar todos los documentos de una Collection
        newpapersCollectionReference
                .get()
                .addOnCompleteListener(
                        object: OnCompleteListener<QuerySnapshot> {
                            override fun onComplete(task: Task<QuerySnapshot>) {
                                if (task.isSuccessful) {
                                    for (newspapersDocument in task.result.documents) {
                                        var newspaperReceived = Newspaper(
                                                nameNewspaper = newspapersDocument.id,
                                                stateNewspaper = newspapersDocument.data?.getValue("estado") as Long,
                                                imageNewspaper = newspapersDocument.data?.getValue("imagen") as String,
                                                nameClaseParserNewspaper = newspapersDocument.data?.getValue("nombreClaseParser") as String,
                                                urlNewspaper =  newspapersDocument.data?.getValue("url") as String
                                        )
                                        Log.d("Recuperar documentos:",
                                                "=>" +newspaperReceived.nameNewspaper+"<="
                                                        + " Estado:-" + newspaperReceived.stateNewspaper + "-"
                                                        + " Imagen:-" + newspaperReceived.imageNewspaper + "-"
                                                        + " Url:-" + newspaperReceived.urlNewspaper + "-"
                                        )
                                        newspapers.add(newspaperReceived)

                                    }
                                } else {
                                    //newspapers.add(newspaperEmpty)
                                    Log.d("Recuperar documentos", "Error getting documents: ", task.exception)
                                }
                                newspapersListener.onRespuesta(newspapers)
                                Log.d("Documentos onComplete", "-"+newspapers.size.toString() + "=>")
                            }
                        }
                )
        Log.d("Documentos ", "-"+newspapers.size.toString() + "=>")
    }

    override fun readCCAANewspapers(country:String,
                                    autonomous_communities: String,
                                    newspapersListener: NewspapersDatabase.NewspapersListener) {
        var newspapers : ArrayList <Newspaper> = ArrayList()
        var newpapersCollectionReferenceCCAA = newpapersCollectionReference.
                document(country).
                collection(autonomous_communities)
        //Recuperar todos los documentos de una Collection
        newpapersCollectionReferenceCCAA
                .get()
                .addOnCompleteListener(
                        object: OnCompleteListener<QuerySnapshot> {
                            override fun onComplete(task: Task<QuerySnapshot>) {
                                if (task.isSuccessful) {
                                    for (newspapersDocument in task.result.documents) {
                                        var newspaperReceived = Newspaper(
                                                nameNewspaper = newspapersDocument.id,
                                                stateNewspaper = newspapersDocument.data?.getValue("estado") as Long,
                                                imageNewspaper = newspapersDocument.data?.getValue("imagen") as String,
                                                nameClaseParserNewspaper = newspapersDocument.data?.getValue("nombreClaseParser") as String,
                                                urlNewspaper =  newspapersDocument.data?.getValue("url") as String
                                        )
                                        Log.d("Recuperar documentos:",
                                                "=>" +newspaperReceived.nameNewspaper+"<="
                                                        + " Estado:-" + newspaperReceived.stateNewspaper + "-"
                                                        + " Imagen:-" + newspaperReceived.imageNewspaper + "-"
                                                        + " Url:-" + newspaperReceived.urlNewspaper + "-"
                                        )
                                        newspapers.add(newspaperReceived)

                                    }
                                } else {
                                    //newspapers.add(newspaperEmpty)
                                    Log.d("Recuperar documentos", "Error getting documents: ", task.exception)
                                }
                                newspapersListener.onRespuesta(newspapers)
                                Log.d("Documentos onComplete", "-"+newspapers.size.toString() + "=>")
                            }
                        }
                )
        Log.d("Documentos ", "-"+newspapers.size.toString() + "=>")
    }
}
/*
override fun readNewspaper(newspapersId: String, newspapersListener: NewspapersDatabase.NewspapersListener) {
    newpapersCollectionReference.document(newspapersId).get().addOnCompleteListener(
            object:OnCompleteListener <DocumentSnapshot>{
                override fun onComplete(@NotNull task:Task<DocumentSnapshot>){
                    if(task.isSuccessful()){
                        var newspaper: Newspaper? = task.getResult().toObject(Newspaper::class.java)
                        Log.d("Newspaper Recuperado:", newspaper?.urlNewspaper)
                        //newspapersListener.onRespuesta(newspaper)
                    }else{
                        //newspapersListener.onRespuesta(newspaperEmpty)
                    }
                }
            }
    )
}
*/
