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


class NewspapersAdapter(
        var context : Context
)
    : RecyclerView.Adapter<NewspapersAdapter.ViewHolder>(){
    val TAG = "Mislugares"
    var items : ArrayList <DocumentSnapshot> = ArrayList()
    lateinit var registration : ListenerRegistration
    var inflador : LayoutInflater
    lateinit var onClickListener : View.OnClickListener

    init{
        items = ArrayList<DocumentSnapshot>()
        inflador = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewspapersAdapter.ViewHolder {
        val v = inflador.inflate(R.layout.individual_newspaper, null)
        v.setOnClickListener(onClickListener)
        return NewspapersAdapter.ViewHolder(v)
    }

    override fun onBindViewHolder(holder: NewspapersAdapter.ViewHolder, posicion: Int) {
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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var newspaperName: TextView = itemView.findViewById(R.id.title_individual_newspaper) as TextView
        var newspaperImage: ImageView = itemView.findViewById(R.id.image_individual_newspaper) as ImageView
    }
}