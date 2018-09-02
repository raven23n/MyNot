package org.frocu.news.mynot.Adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.StorageReference
import org.frocu.news.mynot.POJO.Section
import org.frocu.news.mynot.R
import org.frocu.news.mynot.Singletons.FirebaseToolsInstance.instanceStorageRef
import org.frocu.news.mynot.Singletons.SectionList
import org.frocu.news.mynot.Singletons.SectionList.sections


class SectionAdapter(
        var context : Context
)
    : RecyclerView.Adapter<SectionAdapter.ViewHolder>(){

    var inflater : LayoutInflater
    lateinit var onClickListener : View.OnClickListener
    lateinit var onLongClickListener : View.OnLongClickListener

    init{
        inflater= context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionAdapter.ViewHolder {
        val v = inflater.inflate(R.layout.individual_section, null)
        v.setOnClickListener(onClickListener)
//        v.setOnLongClickListener(onLongClickListener)
        return SectionAdapter.ViewHolder(v)
    }

    override fun onBindViewHolder(holder: SectionAdapter.ViewHolder, position: Int) {
        var thereIsConnection: Boolean? = true
        val objIncome = sections.get(position)
        holder.sectionTitle.text = objIncome.sectionName
        thereIsConnection = isNetworkConnected()
        if (thereIsConnection) {
            var urlSectionImage = instanceStorageRef.child("Secciones/"+objIncome.sectionName+".png")
            val downloadSize = (140 * 1024).toLong()
            urlSectionImage.getBytes(downloadSize)
                    .addOnSuccessListener {bytes ->
                        val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        holder.sectionImage.setImageBitmap(bmp)
                        Log.d("SectionAdapter", "URL Descarga OK")
                    }
                    .addOnFailureListener { exception ->
                        Log.d("SectionAdapter", "URL Descarga Error")
                        holder.sectionImage.setImageResource(R.drawable.no_image)
                    }

        } else {
            holder.sectionImage.setImageResource(R.drawable.no_image)
            Log.d("SectionAdapter", "URL Descarga No internet")
        }
    }

    fun getItem(pos: Int): Section? {
        return SectionList.getSectionItem(pos)
    }

    fun getKey(pos: Int): String {
        return SectionList.getSectionKey(pos)
    }

    override fun getItemCount(): Int {
        return SectionList.getSectionCount()
    }

    fun setOnItemClickListener(onClick: View.OnClickListener) {
        onClickListener = onClick
    }

    fun setOnItemLongClickListener(onLongClick: View.OnLongClickListener) {
        onLongClickListener = onLongClick
    }

    private fun isNetworkConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = cm.activeNetworkInfo
        return ni != null
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var sectionTitle: TextView = itemView.findViewById(R.id.section_title) as TextView
        var sectionImage: ImageView = itemView.findViewById(R.id.section_image) as ImageView
    }
}