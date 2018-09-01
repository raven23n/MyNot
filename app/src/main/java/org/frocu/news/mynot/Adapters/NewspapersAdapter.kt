package org.frocu.news.mynot.Adapters

import android.content.Context
import android.net.ConnectivityManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageLoader
import org.frocu.news.mynot.POJO.Newspaper
import org.frocu.news.mynot.R
import org.frocu.news.mynot.Singletons.NewspapersList
import org.frocu.news.mynot.Singletons.NewspapersList.newspapers
import org.frocu.news.mynot.Singletons.ImageLoaderVolley.imageLoader


class NewspapersAdapter(
        var context : Context
)
    : RecyclerView.Adapter<NewspapersAdapter.ViewHolder>(){

    var inflater : LayoutInflater
    lateinit var onClickListener : View.OnClickListener

    init{
        inflater= context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewspapersAdapter.ViewHolder {
        val v = inflater.inflate(R.layout.individual_newspaper, parent,false)
        v.setOnClickListener(onClickListener)
        return NewspapersAdapter.ViewHolder(v)
    }

    override fun onBindViewHolder(holder: NewspapersAdapter.ViewHolder, position: Int) {
        val objIncome = newspapers.get(position)
        holder.newspaperName.text = objIncome.nameNewspaper

        var thereIsConnection = isNetworkConnected()
        if (thereIsConnection) {
            imageLoader.get(objIncome.imageNewspaper, object : ImageLoader.ImageListener {
                override fun onResponse(response: ImageLoader.ImageContainer, isImmediate: Boolean) {
                    val bitmap = response.bitmap
                    if (bitmap != null) {
                        holder.newspaperImage.apply {
                            setImageBitmap(bitmap)
                            invalidate()
                        }
                    }
                }

                override fun onErrorResponse(error: VolleyError) {
                    holder.newspaperImage.setImageResource(R.drawable.no_image)
                }
            })
        } else {
            holder.newspaperImage.setImageResource(R.drawable.no_image)
        }
    }

    fun getItem(pos: Int): Newspaper? {
        return NewspapersList.getItem(pos)
    }

    fun getKey(pos: Int): String {
        return NewspapersList.getKey(pos)
    }

    override fun getItemCount(): Int {
        return NewspapersList.getItemCount()
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
        var newspaperName: TextView = itemView.findViewById(R.id.title_individual_newspaper) as TextView
        var newspaperImage: ImageView = itemView.findViewById(R.id.image_individual_newspaper) as ImageView

    }
}