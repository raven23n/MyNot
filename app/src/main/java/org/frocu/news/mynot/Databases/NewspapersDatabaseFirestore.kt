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

class NewspapersDatabaseFirestore(val section: String) : NewspapersDatabase{

    private lateinit var newpapersCollectionReference : CollectionReference

    init {
        var sectionDB:FirebaseFirestore = FirebaseFirestore.getInstance()
        newpapersCollectionReference = sectionDB.collection(section)
    }

    override fun readNewspaper(newspapersId: String, newspapersListener: NewspapersDatabase.NewspapersListener) {
/*        val docRef = db.collection("notes").document("note-id")
        docRef.get().addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: " + task.result.data)
                } else {
                    Log.d(TAG, "No such document")
                }
            } else {
                Log.d(TAG, "get failed with ", task.exception)
            }
        })

        // custom object
        val docRef = db.collection("notes").document("note-id")
        docRef.get().addOnSuccessListener(OnSuccessListener<DocumentSnapshot> { documentSnapshot ->
            val note = documentSnapshot.toObject(Note::class.java)
        })*/

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
                        }
                    } else {
                        Log.d("Recuperar documentos", "Error getting documents: ", task.exception)
                    }
                })
        return newspapers
    }
}