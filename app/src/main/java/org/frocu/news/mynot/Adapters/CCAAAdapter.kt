package org.frocu.news.mynot.Adapters

import android.content.Context
import android.net.ConnectivityManager
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.frocu.news.mynot.POJO.Section
import org.frocu.news.mynot.R
import org.frocu.news.mynot.Singletons.CCAAList
import org.frocu.news.mynot.Singletons.CCAAList.ccaaList

class CCAAAdapter(
        var context : Context
)
    : RecyclerView.Adapter<CCAAAdapter.ViewHolder>(){

    var inflater : LayoutInflater
    lateinit var onClickListener : View.OnClickListener

    init{
        inflater= context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CCAAAdapter.ViewHolder {
        val v = inflater.inflate(R.layout.individual_section, null)
        v.setOnClickListener(onClickListener)
        return CCAAAdapter.ViewHolder(v)
    }

    override fun onBindViewHolder(holder: CCAAAdapter.ViewHolder, position: Int) {

        val objIncome = ccaaList.get(position)
        holder.sectionTitle.text = objIncome.sectionName


        var string1 = objIncome.sectionName.toLowerCase()
        var string2 = string1.replace("-","_")
                .replace("."," ")
                .replace("ñ","n")
                .replace("á","a")
                .replace("é","e")
                .replace("í","i")
                .replace("ó","o")
                .replace("ú","u")
                .replace(" ","")
        var uriImage = "@drawable/" + string2
        val imageId = context.resources.getIdentifier(uriImage,null,context.getPackageName())
        holder.sectionImage.setImageResource(imageId)
    }

    fun getItem(pos: Int): Section? {
        return CCAAList.getCCAAItem(pos)
    }

    fun getKey(pos: Int): String {
        return CCAAList.getCCAAKey(pos)
    }

    override fun getItemCount(): Int {
        return CCAAList.getCCAACount()
    }

    fun setOnItemClickListener(onClick: View.OnClickListener) {
        onClickListener = onClick
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