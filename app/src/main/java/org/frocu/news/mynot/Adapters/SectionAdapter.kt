package org.frocu.news.mynot.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.frocu.news.mynot.POJO.Section
import org.frocu.news.mynot.R
import org.frocu.news.mynot.Singletons.SectionList
import org.frocu.news.mynot.Singletons.SectionList.sections
import android.support.v4.content.ContextCompat
import android.text.TextUtils.replace

class SectionAdapter(
        var context : Context
)
    : RecyclerView.Adapter<SectionAdapter.ViewHolder>(){

    var inflater : LayoutInflater
    lateinit var onClickListener : View.OnClickListener

    init{
        inflater= context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionAdapter.ViewHolder {
        val v = inflater.inflate(R.layout.individual_section, null)
        v.setOnClickListener(onClickListener)
        return SectionAdapter.ViewHolder(v)
    }

    @SuppressLint("Range")
    override fun onBindViewHolder(holder: SectionAdapter.ViewHolder, position: Int) {
        val objIncome = sections.get(position)
        holder.sectionTitle.text = objIncome.sectionName
        var titleColor = objIncome.sectionColour
        holder.sectionTitle.setBackgroundColor(Color.parseColor(titleColor))
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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var sectionTitle: TextView = itemView.findViewById(R.id.section_title) as TextView
        var sectionImage: ImageView = itemView.findViewById(R.id.section_image) as ImageView
    }
}