package org.frocu.news.mynot.Adapters

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.support.v4.util.LruCache
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import org.frocu.news.mynot.POJO.Newspaper
import org.frocu.news.mynot.R
import org.frocu.news.mynot.Singletons.NewspapersList.newspapers


class NewspapersAdapter(
        var context : Context
)
    : RecyclerView.Adapter<NewspapersAdapter.ViewHolder>(){

    val requestQueue: RequestQueue
    var inflater : LayoutInflater
    lateinit var onClickListener : View.OnClickListener
    private var imageLoader: ImageLoader

    init{
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        requestQueue = Volley.newRequestQueue(context)
        imageLoader = ImageLoader(requestQueue, object : ImageLoader.ImageCache {
            private val cache = LruCache<String, Bitmap>(10)

            override fun putBitmap(url: String, bitmap: Bitmap) {
                cache.put(url, bitmap)
            }

            override fun getBitmap(url: String): Bitmap {
                return cache.get(url)
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewspapersAdapter.ViewHolder {
        val v = inflater.inflate(R.layout.individual_newspaper, parent,false)
        v.setOnClickListener(onClickListener)
        return NewspapersAdapter.ViewHolder(v)
    }

    override fun onBindViewHolder(holder: NewspapersAdapter.ViewHolder, position: Int) {
        var thereIsConnection: Boolean? = true
        val objIncome = newspapers.get(position)
        holder.newspaperName.text = objIncome.nameNewspaper

        thereIsConnection = isNetworkConnected()
        if (thereIsConnection) {
            imageLoader.get(objIncome.imageNewspaper, object : ImageLoader.ImageListener {
                override fun onResponse(response: ImageLoader.ImageContainer, isImmediate: Boolean) {
                    val bitmap = response.bitmap
                    holder.newspaperImage.setImageBitmap(bitmap)
                    holder.newspaperImage.invalidate()
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
        return newspapers[pos]
    }

    fun getKey(pos: Int): String {
        return newspapers[pos].nameNewspaper
    }

    override fun getItemCount(): Int {
        return newspapers.size
    }

    fun setOnItemClickListener(onClick: View.OnClickListener) {
        onClickListener = onClick
    }

    private fun isNetworkConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = cm.activeNetworkInfo
        return ni != null
        /* if (ni == null) {
                false
           } else
                true*/
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var newspaperName: TextView = itemView.findViewById(R.id.title_individual_newspaper) as TextView
        var newspaperImage: ImageView = itemView.findViewById(R.id.image_individual_newspaper) as ImageView
    }
}