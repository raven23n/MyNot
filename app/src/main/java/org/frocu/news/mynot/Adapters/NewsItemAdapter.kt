package org.frocu.news.mynot.Adapters

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
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
import org.frocu.news.mynot.Singletons.GlobalVariablesAndFuns
import org.frocu.news.mynot.Singletons.GlobalVariablesAndFuns.isNetworkConnected
import org.frocu.news.mynot.Singletons.GlobalVariablesAndFuns.positionNewspaperInCharge
import org.frocu.news.mynot.Singletons.NewsItemList
import org.frocu.news.mynot.Singletons.NewsItemList.news
import org.frocu.news.mynot.Singletons.ImageLoaderVolley.imageLoader

class NewsItemAdapter(
        var context : Context
)
    : RecyclerView.Adapter<NewsItemAdapter.ViewHolder>(){

    var inflater : LayoutInflater
    lateinit var onClickListener : View.OnClickListener
    lateinit var onLongClickListener : View.OnLongClickListener

    init{
        inflater= context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemAdapter.ViewHolder {
        val v = inflater.inflate(R.layout.individual_news_item, null)
        v.setOnClickListener(onClickListener)
        v.setOnLongClickListener(onLongClickListener)
//        v.setBackgroundResource(R.drawable.individual_border)
        if(positionNewspaperInCharge != -1)
            v.setBackgroundColor((Color.parseColor(GlobalVariablesAndFuns.colorActual)))
        return NewsItemAdapter.ViewHolder(v)
    }

    override fun onBindViewHolder(holder: NewsItemAdapter.ViewHolder, position: Int) {
        val objIncome = news.get(position)
        holder.headlineOfANewsIndividual.text = objIncome.headlineOfANews
        holder.dateOfANewsIndividual.text = objIncome.dateOfANews
        if (isNetworkConnected(context)) {
            imageLoader.get(objIncome.imageOfANews, object : ImageLoader.ImageListener {
                override fun onResponse(response: ImageLoader.ImageContainer, isImmediate: Boolean) {
                    val bitmap = response.bitmap
                    if (bitmap != null) {
                        holder.apply {
                            imageOfANewsIndividual.setImageBitmap(bitmap)
                            imageOfANewsIndividual.invalidate()
                        }
                    }
                }

                override fun onErrorResponse(error: VolleyError) {
                    holder.imageOfANewsIndividual.setImageResource(R.drawable.no_image)
                }
            })
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

    fun setOnItemLongClickListener(onLongClick: View.OnLongClickListener) {
        onLongClickListener = onLongClick
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var headlineOfANewsIndividual: TextView = itemView.findViewById(R.id.headline_of_a_news_individual) as TextView
        var dateOfANewsIndividual: TextView = itemView.findViewById(R.id.date_of_a_news_individual) as TextView
        var imageOfANewsIndividual: ImageView = itemView.findViewById(R.id.image_of_a_news_individual) as ImageView

    }
}