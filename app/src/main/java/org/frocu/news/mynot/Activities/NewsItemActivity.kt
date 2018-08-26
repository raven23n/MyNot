package org.frocu.news.mynot.Activities

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import org.frocu.news.mynot.Adapters.NewspapersAdapter
import org.frocu.news.mynot.POJO.NewsItem
import org.frocu.news.mynot.R
import org.frocu.news.mynot.Singletons.NewspapersList.newspapers
import org.xml.sax.SAXException
import java.io.IOException
import java.util.ArrayList
import javax.xml.parsers.ParserConfigurationException
import javax.xml.parsers.SAXParserFactory

class NewsItemActivity : AppCompatActivity()  {
    lateinit var newsItemAdapter : NewspapersAdapter
    lateinit var newsItemLayoutManager: RecyclerView.LayoutManager
    lateinit var newsItemRecyclerView: RecyclerView
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

    internal class AccessToNews: AsyncTask<String, Void, ArrayList<NewsItem>>() {

        override fun onPreExecute() {
        }

        override fun onProgressUpdate(vararg values: Void) {

        }

        override fun doInBackground(vararg params: String): ArrayList<NewsItem> {

            var news: ArrayList<NewsItem> = ArrayList()
            try {
                val factory = SAXParserFactory.newInstance()
                val parser = factory.newSAXParser()
                /*val handler = SAXParserHandler()
                parser.parse(params[0], handler)
                news = handler.noticias*/
            } catch (e: ParserConfigurationException) {
                e.printStackTrace()
                val noticiaActual = NewsItem(
                        headlineOfANews = "Error al cargar la página.",
                        imageOfANews = "",
                        urlOfANews = "",
                        dateOfANews = ""
                )
                news.add(noticiaActual)
            } catch (e: SAXException) {
                e.printStackTrace()
                val noticiaActual = NewsItem(
                        headlineOfANews = "Error al transformar la página.",
                        imageOfANews = "",
                        urlOfANews = "",
                        dateOfANews = ""
                )
                news.add(noticiaActual)
            } catch (e: IOException) {
                e.printStackTrace()
                val noticiaActual = NewsItem(
                        headlineOfANews = "No se tiene acceso a internet.",
                        imageOfANews = "",
                        urlOfANews = "",
                        dateOfANews = ""
                )
                news.add(noticiaActual)
            } catch (e: Exception) {
                e.printStackTrace()
                val noticiaActual = NewsItem(
                        headlineOfANews = "Error general al cargar la página.",
                        imageOfANews = "",
                        urlOfANews = "",
                        dateOfANews = ""
                )
                news.add(noticiaActual)
            }

            return news
        }


        override fun onPostExecute(noticias: ArrayList<NewsItem>) {
/*            val itrNot = noticias.iterator()
            listaNoticias = ArrayList<NewsItem>()
            while (itrNot.hasNext()) {
                val miNoticia = itrNot.next() as NewsItem
                miNoticia.setPeriodico(miPeriodico)
                listaNoticias.add(miNoticia)
            }
            newsItemAdapter

            recyclerView = findViewById<View>(R.id.recycler_view) as RecyclerView
            adaptador = AdaptadorNoticias(this@TitularesActivity, listaNoticias)
            recyclerView.setAdapter(adaptador)
            adaptador.setOnItemClickListener(View.OnClickListener { v ->
                val pos = recyclerView.getChildAdapterPosition(v)
                val s = listaNoticias.get(pos).toString()
                val intent = Intent(getApplicationContext(), ActividadNoticia::class.java)
                intent.putExtra("titular", listaNoticias.get(pos).getTitular())
                intent.putExtra("fecha", listaNoticias.get(pos).getFecha())
                intent.putExtra("cuerpo", listaNoticias.get(pos).getCuerpo())
                intent.putExtra("periodico", listaNoticias.get(pos).getPeriodico())
                intent.putExtra("url", listaNoticias.get(pos).getUrl())
                intent.putExtra("tipo", tipo)
                ContextCompat.startActivity(intent)
            })
            adaptador.notifyDataSetChanged()*/
        }
    }
}