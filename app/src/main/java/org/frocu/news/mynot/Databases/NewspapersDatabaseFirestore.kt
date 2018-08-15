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
import org.jetbrains.annotations.NotNull

class NewspapersDatabaseFirestore(val section: String, val escuchador : NewspapersDatabase.NewspapersListener)
    : NewspapersDatabase{

    private lateinit var newpapersCollectionReference : CollectionReference

    init {
        var sectionDB:FirebaseFirestore = FirebaseFirestore.getInstance()
        newpapersCollectionReference = sectionDB.collection(section)
    }

    override fun readNewspaper(newspapersId: String, newspapersListener: NewspapersDatabase.NewspapersListener) {
        newpapersCollectionReference.document(newspapersId).get().addOnCompleteListener(
            object:OnCompleteListener <DocumentSnapshot>{
                override fun onComplete(@NotNull task:Task<DocumentSnapshot>){
                    if(task.isSuccessful()){
                        var newspaper: Newspaper? = task.getResult().toObject(Newspaper::class.java)
                        Log.d("Newspaper Recuperado:", newspaper?.urlNewspaper)
                        newspapersListener.onRespuesta(newspaper)
                    }else{
                        newspapersListener.onRespuesta(null)
                    }
                }
            }
        )
    }

    override fun readNewspapers(): ArrayList<Newspaper> {
        var newspapers : ArrayList <Newspaper> = ArrayList()
        //Recuperar todos los documentos de una Collection
        newpapersCollectionReference
                .get()
                .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                    if (task.isSuccessful) {
                        for (newspaper in task.result) {
                            Log.d("Recuperar documentos", newspaper.id + " => " + newspaper.data)
                            var newspaperReceived: Newspaper = Newspaper(
                                    nameNewspaper = newspaper.id,
                                    stateNewspaper = newspaper.data.getValue("estado") as Long,
                                    imageNewspaper = newspaper.data.getValue("imagen") as String,
                                    nameClaseParserNewspaper = newspaper.data.getValue("nombreClaseParser") as String,
                                    urlNewspaper =  newspaper.data.getValue("url") as String
                            )
                            escuchador.onRespuesta(newspaperReceived)
                            newspapers.add(newspaperReceived)
                        }
                    } else {
                        Log.d("Recuperar documentos", "Error getting documents: ", task.exception)
                    }
                })
        return newspapers
    }
}