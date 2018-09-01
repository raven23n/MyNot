package org.frocu.news.mynot.Activities

import android.content.Intent
import android.os.AsyncTask
import android.os.AsyncTask.execute
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import org.frocu.news.mynot.Adapters.NewsItemAdapter
import org.frocu.news.mynot.POJO.NewsItem
import org.frocu.news.mynot.ParserHandlers.SAXParserHandler
import org.frocu.news.mynot.Singletons.NewsItemList.news
import org.frocu.news.mynot.Singletons.NewspapersList.newspapers
import org.xml.sax.SAXException
import java.io.IOException
import javax.xml.parsers.ParserConfigurationException
import javax.xml.parsers.SAXParserFactory
import org.frocu.news.mynot.R
import org.frocu.news.mynot.R.id.recycler_view_news_item
import org.frocu.news.mynot.Singletons.ImageLoaderVolley.initializeImageLoaderVolley
import org.frocu.news.mynot.Singletons.GlobalVariables.positionNewspaperInCharge
import org.frocu.news.mynot.Singletons.GlobalVariables.urlNewsItemActual

import java.util.ArrayList
import android.view.MenuInflater
import android.view.ContextMenu.ContextMenuInfo
import android.view.ContextMenu
import android.view.MenuItem
import android.widget.Toast


class NewsItemActivity : AppCompatActivity()  {

    lateinit var newsItemAdapter : NewsItemAdapter
    lateinit var newsItemLayoutManager: RecyclerView.LayoutManager
    lateinit var newsItemRecyclerView: RecyclerView

    init{

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_news_item)
        Log.d("NewsItemActivity", "Entro en onCreate")
    }

    override fun onResume() {
        super.onResume()
        executeAccessToNews()
        Log.d("NewsItemActivity", "Entro en onResume")
        Log.d("NewsItemActivity", "positionNewspaperInCharge: -" + positionNewspaperInCharge +"-")
    }

    fun executeAccessToNews(){
        AccessToNews().execute(newspapers[positionNewspaperInCharge].urlNewspaper)
    }
    fun startWebNavigatorActivity(urlNews: String) {
        val intent = Intent(this, NewsWebViewActivity::class.java)
        urlNewsItemActual = urlNews
        startActivity(intent)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.context_menu_news_item, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        //find out which menu item was pressed
        when (item.getItemId()) {
            R.id.save_news_item_option -> {
                saveNewsItem()
                return true
            }
            else -> return false
        }
    }

    fun saveNewsItem(){
        Toast.makeText(this,"Noticia guardada para su posterior consulta.",Toast.LENGTH_LONG).show()
    }

    inner class AccessToNews: AsyncTask<String, Void, ArrayList<NewsItem>>() {

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
                Log.d("NewsItemActivity URL", "-" + params[0] + "-")
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

            initializeImageLoaderVolley(this@NewsItemActivity)
            newsItemRecyclerView = findViewById(recycler_view_news_item) as RecyclerView
            Log.d("NewsItemActivity", "Recycler view asignado")
            registerForContextMenu(newsItemRecyclerView);
            Log.d("NewsItemActivity", "Resgitro el context menú")
            newsItemAdapter = NewsItemAdapter(this@NewsItemActivity)
            Log.d("NewsItemActivity", "Creo adaptador")
            newsItemAdapter.setOnItemClickListener(View.OnClickListener { v ->
                val pos = newsItemRecyclerView.getChildAdapterPosition(v)
                Log.d("NewsItemActivity", "Posicion Elemento -"+pos+"-")
                val urlNews = news.get(pos).urlOfANews
                startWebNavigatorActivity(urlNews)
            })
            newsItemRecyclerView.adapter= newsItemAdapter
            Log.d("NewsItemActivity", "Recycler view con adaptador")
            newsItemLayoutManager = LinearLayoutManager(this@NewsItemActivity)
            Log.d("NewsItemActivity", "Creo LinearLayoutManager")
            newsItemRecyclerView.layoutManager = newsItemLayoutManager
            Log.d("NewsItemActivity", "Recycler view con LinearLayoutManager asignado")
            newsItemAdapter.notifyDataSetChanged()
        }
    }
}