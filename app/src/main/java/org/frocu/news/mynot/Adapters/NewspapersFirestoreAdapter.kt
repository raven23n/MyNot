package org.frocu.news.mynot.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.google.firebase.firestore.*
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.frocu.news.mynot.POJO.Newspaper
import org.frocu.news.mynot.R
import com.google.firebase.firestore.DocumentSnapshot
import android.util.Log
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import java.lang.Math.min
import java.lang.Math.abs


class NewspapersFirestoreAdapter(
        var context : Context,
        var query : Query
)
    : RecyclerView.Adapter<NewspapersFirestoreAdapter.ViewHolder>(),
        EventListener<QuerySnapshot> {
    val TAG = "Mislugares"
    var items : ArrayList <DocumentSnapshot> = ArrayList()
    lateinit var registration : ListenerRegistration
    var inflador : LayoutInflater
    lateinit var onClickListener : View.OnClickListener

    init{
        items = ArrayList<DocumentSnapshot>()
        this.query = query
        inflador = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewspapersFirestoreAdapter.ViewHolder {
        val v = inflador.inflate(R.layout.individual_newspaper, null)
        v.setOnClickListener(onClickListener)
        return NewspapersFirestoreAdapter.ViewHolder(v)
    }

    override fun onBindViewHolder(holder: NewspapersFirestoreAdapter.ViewHolder, posicion: Int) {
        val lugar = getItem(posicion)
//        personalizaVista(holder, lugar)
    }

    fun getItem(pos: Int): Newspaper? {
        return items[pos].toObject(Newspaper::class.java)
    }

    fun getKey(pos: Int): String {
        return items[pos].id
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setOnItemClickListener(onClick: View.OnClickListener) {
        onClickListener = onClick
    }

    fun startListening() {
        items = ArrayList()
        registration = query.addSnapshotListener(this)
    }

    fun stopListening() {
        registration.remove()
    }

    override fun onEvent(snapshots: QuerySnapshot?, e: FirebaseFirestoreException?) {
        if (e != null) {
            Log.w(TAG, "error al recibir evento", e)
            return
        }
        for (dc in snapshots!!.documentChanges) {
            val pos = dc.newIndex
            val oldPos = dc.oldIndex
            when (dc.type) {
                DocumentChange.Type.ADDED -> {
                    items.add(pos, dc.document)
                    notifyItemInserted(pos)
                }
                DocumentChange.Type.REMOVED -> {
                    items.removeAt(oldPos)
                    notifyItemRemoved(oldPos)
                }
                DocumentChange.Type.MODIFIED -> {
                    items.removeAt(oldPos)
                    items.add(pos, dc.document)
                    notifyItemRangeChanged(min(pos, oldPos), abs(pos - oldPos) + 1)
                }
                else -> Log.w(TAG, "Tipo de cambio desconocido", e)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var newspaperName: TextView = itemView.findViewById(R.id.title_individual_newspaper) as TextView
        var newspaperImage: ImageView = itemView.findViewById(R.id.image_individual_newspaper) as ImageView
    }
}