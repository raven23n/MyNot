package org.frocu.news.mynot.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.google.firebase.firestore.*

class NewspapersFirestoreAdapter(
        var context : Context,
        var query : Query
)
/*    : RecyclerView.Adapter<NewspapersAdapter.ViewHolder>,
        EventListener<QuerySnapshot> */{
    val TAG = "Mislugares"
    var items : List <DocumentSnapshot> = ArrayList<DocumentSnapshot>()
    lateinit var registration : ListenerRegistration
    lateinit var inflador : LayoutInflater
    lateinit var onClickListener : View.OnClickListener

    init{

    }
}