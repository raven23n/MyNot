package org.frocu.news.mynot.Activities

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import org.frocu.news.mynot.Adapters.NewspapersAdapter
import org.frocu.news.mynot.POJO.NewsItem
import org.frocu.news.mynot.ParserHandlers.SAXParserHandler
import org.frocu.news.mynot.R
import org.xml.sax.SAXException
import java.io.IOException
import java.util.ArrayList
import javax.xml.parsers.ParserConfigurationException
import javax.xml.parsers.SAXParserFactory
import org.frocu.news.mynot.R.id.recycler_view_news_item
import org.frocu.news.mynot.Singletons.NewsItemList.news
import org.frocu.news.mynot.Singletons.NewspapersList.newspapers

class NewsItemActivity : AppCompatActivity()  {
    var newspaperPosition: Int = 0
    private var accessToNews = AccessToNews()
    init{

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_news_item)
        Log.d("NewsItemActivity", "Entro en onCreate")
    }

    override fun onResume() {
        super.onResume()
        /*newsItemRecyclerView = findViewById(R.id.recycler_view_newspapers) as RecyclerView
        Log.d("NewsItemActivity", "Recycler view asignado")
        newsItemAdapter = NewspapersAdapter(this)
        Log.d("NewsItemActivity", "Creo adaptador")
        newsItemAdapter.setOnItemClickListener(View.OnClickListener { v ->
            val pos = newsItemRecyclerView.getChildAdapterPosition(v)
            Log.d("NewsItemActivity", "Posicion Elemento -"+pos+"-")
        })
        newsItemRecyclerView.adapter= newsItemAdapter
        Log.d("NewsItemActivity", "Recycler view con adaptador")
        newsItemLayoutManager = LinearLayoutManager(this)
        Log.d("NewsItemActivity", "Creo LinearLayoutManager")
        newsItemRecyclerView.layoutManager = newsItemLayoutManager
        Log.d("NewsItemActivity", "Recycler view con LinearLayoutManager asignado")
        newsItemAdapter.notifyDataSetChanged()*/
        val extras = intent.extras
        newspaperPosition = extras.getInt("position")
        accessToNews.execute(newspapers[newspaperPosition].urlNewspaper)
        Log.d("NewsItemActivity", "Entro en onResume")
    }

    fun getContext(): Context{
        return NewsItemActivity().getContext()
    }

    internal class AccessToNews: AsyncTask<String, Void, ArrayList<NewsItem>>() {
        lateinit var newsItemAdapter : NewspapersAdapter
        lateinit var newsItemLayoutManager: RecyclerView.LayoutManager
        var newsItemRecyclerView: RecyclerView

        init {
            newsItemRecyclerView = recycler_view_news_item as RecyclerView
        }

        override fun onPreExecute() {
        }

        override fun onProgressUpdate(vararg values: Void) {

        }

        override fun doInBackground(vararg params: String): ArrayList<NewsItem> {

            news.clear()
            try {
                val factory = SAXParserFactory.newInstance()
                val parser = factory.newSAXParser()
                val handler = SAXParserHandler()
                parser.parse(params[0], handler)
            } catch (e: ParserConfigurationException) {
                e.printStackTrace()
                val errorNews = NewsItem(
                        headlineOfANews = "Error al cargar la página.",
                        imageOfANews = "",
                        urlOfANews = "",
                        dateOfANews = ""
                )
                news.add(errorNews)
            } catch (e: SAXException) {
                e.printStackTrace()
                val errorNews = NewsItem(
                        headlineOfANews = "Error al transformar la página.",
                        imageOfANews = "",
                        urlOfANews = "",
                        dateOfANews = ""
                )
                news.add(errorNews)
            } catch (e: IOException) {
                e.printStackTrace()
                val errorNews = NewsItem(
                        headlineOfANews = "No se tiene acceso a internet.",
                        imageOfANews = "",
                        urlOfANews = "",
                        dateOfANews = ""
                )
                news.add(errorNews)
            } catch (e: Exception) {
                e.printStackTrace()
                val errorNews = NewsItem(
                        headlineOfANews = "Error general al cargar la página.",
                        imageOfANews = "",
                        urlOfANews = "",
                        dateOfANews = ""
                )
                news.add(errorNews)
            }
            return news
        }


        override fun onPostExecute(news: ArrayList<NewsItem>) {
            Log.d("NewsItemActivity", "Recycler view asignado")
            newsItemAdapter = NewspapersAdapter(NewsItemActivity().getContext())
            Log.d("NewsItemActivity", "Creo adaptador")
            newsItemAdapter.setOnItemClickListener(View.OnClickListener { v ->
                val pos = newsItemRecyclerView.getChildAdapterPosition(v)
                Log.d("NewsItemActivity", "Posicion Elemento -"+pos+"-")
            })
            newsItemRecyclerView.adapter= newsItemAdapter
            Log.d("NewsItemActivity", "Recycler view con adaptador")
            newsItemLayoutManager = LinearLayoutManager(NewsItemActivity().getContext())
            Log.d("NewsItemActivity", "Creo LinearLayoutManager")
            newsItemRecyclerView.layoutManager = newsItemLayoutManager
            Log.d("NewsItemActivity", "Recycler view con LinearLayoutManager asignado")
            newsItemAdapter.notifyDataSetChanged()
        }
    }
}