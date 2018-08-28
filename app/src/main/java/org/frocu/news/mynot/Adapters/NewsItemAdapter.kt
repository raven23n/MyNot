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
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageLoader
/*import org.frocu.news.mynot.Activities.NewsItemActivity.Companion.imageLoader
import org.frocu.news.mynot.Activities.NewsItemActivity.Companion.requestQueue*/
import org.frocu.news.mynot.POJO.NewsItem
import org.frocu.news.mynot.R
import org.frocu.news.mynot.Singletons.NewsItemList
import org.frocu.news.mynot.Singletons.NewsItemList.news
import org.frocu.news.mynot.Singletons.imageLoaderVolley.imageLoader

class NewsItemAdapter(
        var context : Context
)
    : RecyclerView.Adapter<NewsItemAdapter.ViewHolder>(){

    var inflater : LayoutInflater
    lateinit var onClickListener : View.OnClickListener

    init{
        inflater= context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemAdapter.ViewHolder {
//        val v = inflater.inflate(R.layout.individual_news_item, parent,false)
        val v = inflater.inflate(R.layout.individual_news_item, null)
        v.setOnClickListener(onClickListener)
        return NewsItemAdapter.ViewHolder(v)
    }

    override fun onBindViewHolder(holder: NewsItemAdapter.ViewHolder, position: Int) {
        var thereIsConnection: Boolean? = true
        val objIncome = news.get(position)
        holder.headlineOfANewsIndividual.text = objIncome.headlineOfANews
        holder.dateOfANewsIndividual.text = objIncome.dateOfANews
        thereIsConnection = isNetworkConnected()
        if (thereIsConnection) {
            //en AudioLibroKotlin tengo un ejemplo de esto en kotlin
/*            imageLoader = ImageLoader(requestQueue, object :ImageLoader.ImageCache {
                val cache : LruCache<String,Bitmap> = LruCache(DEFAULT_BUFFER_SIZE)

                override fun putBitmap(url: String, bitmap: Bitmap) {
                    cache.put(url, bitmap)
                }

                override fun getBitmap(url: String): Bitmap {
                    return cache.get(url)
                }
            })*/
            imageLoader.get(objIncome.imageOfANews, object : ImageLoader.ImageListener {
                override fun onResponse(response: ImageLoader.ImageContainer, isImmediate: Boolean) {
                    val bitmap = response.bitmap
                    if (bitmap != null) {
                        holder.apply {
                            imageOfANewsIndividual.setImageBitmap(bitmap)
                            imageOfANewsIndividual.invalidate()
                        }
                    }
                    /*                    holder.imageOfANewsIndividual.setImageBitmap(bitmap)
                    holder.imageOfANewsIndividual.invalidate()*/
                }

                override fun onErrorResponse(error: VolleyError) {
                    holder.imageOfANewsIndividual.setImageResource(R.drawable.no_image)
                }
            })
            //holder.imageOfANewsIndividual.setImageResource(R.drawable.no_image)
/*            var newspaperImageListener = object: NewspapersImageDatabase.NewspaperImageListener{
                override fun onRespuesta(image: Bitmap?) {
                    holder.newspaperImage.setImageBitmap(image)
                }
            }
            var newspaperImageDatabase = NewspapersImageDatabaseStorage(objIncome.nameNewspaper)
            newspaperImageDatabase.takeImageNewspaper(newspaperImageListener = newspaperImageListener)*/
        } else {
            holder.imageOfANewsIndividual.setImageResource(R.drawable.no_image)
        }
    }

    fun getItem(pos: Int): NewsItem? {
        return NewsItemList.getItem(pos)
    }

    fun getKey(pos: Int): String {
        return NewsItemList.getKey(pos)
    }

    override fun getItemCount(): Int {
        return NewsItemList.getItemCount()
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
        var headlineOfANewsIndividual: TextView = itemView.findViewById(R.id.headline_of_a_news_individual) as TextView
        var dateOfANewsIndividual: TextView = itemView.findViewById(R.id.date_of_a_news_individual) as TextView
        var imageOfANewsIndividual: ImageView = itemView.findViewById(R.id.image_of_a_news_individual) as ImageView

    }
}