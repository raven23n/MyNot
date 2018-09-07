package org.frocu.news.mynot.Activities

import android.animation.AnimatorInflater
import android.app.AlertDialog
import android.app.ProgressDialog.show
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.AsyncTask.execute
import android.os.Bundle
import android.support.design.widget.Snackbar
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
import org.frocu.news.mynot.Singletons.LongClickContextMenu.createContextMenu
import android.telecom.Call.Details
import android.text.Html
import org.frocu.news.mynot.Singletons.GlobalVariables
import org.frocu.news.mynot.Singletons.GlobalVariables.colorActual


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
        if(positionNewspaperInCharge.equals(-1)){
            validationNewsItemActivity()
        }else{
            executeAccessToNews()
        }
        Log.d("NewsItemActivity", "Entro en onResume")
        Log.d("NewsItemActivity", "positionNewspaperInCharge: -" + positionNewspaperInCharge +"-")
    }

    fun executeAccessToNews(){
        var accessNewsItemActivity= GlobalVariables.checkSharedPreferencesKey(this, "accessNewsItemActivity")
        if (accessNewsItemActivity == "N") {
            var message = "En esta ventana se mostrarán las noticias del periódico elegido. <br><br>" +
                    "Si pulsas sobre una noticia podrás acceder a la web del periódico y leer la noticia completa.<br><br>" +
                    "Si mantienes pulsada una noticia podrás guardarla para consultarla más tarde o compartirla con quién quieras."
            android.support.v7.app.AlertDialog.Builder(this)
                    .setTitle("MyNot")
                    .setMessage(Html.fromHtml(message))
                    .setPositiveButton("OK", DialogInterface.OnClickListener(){
                        dialogInterface: DialogInterface, i: Int ->
                        fun onClick(dialog: DialogInterface, id:Int){
                            dialog.cancel()
                        }
                    })
                    .show()
        }
        GlobalVariables.updateSharedPreference(this, "accessNewsItemActivity", "S")
        AccessToNews().execute(newspapers[positionNewspaperInCharge].urlNewspaper)
    }
    fun startWebNavigatorActivity(urlNews: String) {
        val intent = Intent(this, NewsWebViewActivity::class.java)
        urlNewsItemActual = urlNews
        Log.d("NewsItemActivity", "urlNewsItemActual -" + urlNewsItemActual + "-")
        startActivity(intent)
    }

    fun newsItemAdapterNotifyDataSetChanged(){
        finish()
        val intent = Intent(this, NewsItemActivity::class.java)
        startActivity(intent)
    }

    fun validationNewsItemActivity(){
        if(news.size<=0){
            AlertDialog.Builder(this)
                    .setTitle("MyNot")
                    .setMessage("No tienes guardada ninguna noticia.")
                    .setPositiveButton("OK", {
                        dialogInterface: DialogInterface, i: Int ->
                            dialogInterface.cancel()
                            Log.d("NewsItemActivity", "Cierro el Dialog")
                            onBackPressed()
                    })
                    .show()
        }else {
            var accessSavedNewsItemActivity= GlobalVariables.checkSharedPreferencesKey(this, "accessSavedNewsItemActivity")
            if (accessSavedNewsItemActivity == "N") {
                var message = "En esta ventana se mostrarán las noticias guardadas. <br><br>" +
                        "Si pulsas sobre una noticia podrás acceder a la web del periódico y leer la noticia completa.<br><br>" +
                        "Si mantienes pulsada una noticia podrás borrarla o compartirla con quién quieras."
                android.support.v7.app.AlertDialog.Builder(this)
                        .setTitle("MyNot")
                        .setMessage(Html.fromHtml(message))
                        .setPositiveButton("OK", DialogInterface.OnClickListener(){
                            dialogInterface: DialogInterface, i: Int ->
                            fun onClick(dialog: DialogInterface, id:Int){
                                dialog.cancel()
                            }
                        })
                        .show()
                GlobalVariables.updateSharedPreference(this, "accessSavedNewsItemActivity", "S")
            }
            chargeNewsItemActivity()
        }
    }

    fun chargeNewsItemActivity(){
        initializeImageLoaderVolley(this@NewsItemActivity)
        newsItemRecyclerView = findViewById(recycler_view_news_item) as RecyclerView
        //newsItemRecyclerView.setBackgroundColor(Color.parseColor(GlobalVariables.colorActual))
        Log.d("NewsItemActivity", "Recycler view asignado")
        Log.d("NewsItemActivity", "colorActual: -"+ colorActual + "-")
        Log.d("NewsItemActivity", "Resgitro el context menú")
        newsItemAdapter = NewsItemAdapter(this@NewsItemActivity)
        Log.d("NewsItemActivity", "Creo adaptador")
        newsItemAdapter.setOnItemClickListener(View.OnClickListener { v ->
            val position = newsItemRecyclerView.getChildAdapterPosition(v)
            Log.d("NewsItemActivity", "Posicion Elemento -" + position + "-")
            val urlNews = news.get(position).urlOfANews
            Log.d("NewsItemActivity", "urlNews -" + urlNews + "-")
            startWebNavigatorActivity(urlNews)
        })
        newsItemAdapter.setOnItemLongClickListener(View.OnLongClickListener { v ->
            val position = newsItemRecyclerView.getChildAdapterPosition(v)
            val menu = createContextMenu(this@NewsItemActivity, position)
            if (menu != null) {
                menu.show()
            }
            true
        })
        newsItemRecyclerView.adapter = newsItemAdapter
        Log.d("NewsItemActivity", "Recycler view con adaptador")
        newsItemLayoutManager = LinearLayoutManager(this@NewsItemActivity)
        Log.d("NewsItemActivity", "Creo LinearLayoutManager")
        newsItemRecyclerView.layoutManager = newsItemLayoutManager
        Log.d("NewsItemActivity", "Recycler view con LinearLayoutManager asignado")
        newsItemAdapter.notifyDataSetChanged()
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
            chargeNewsItemActivity()
/*            initializeImageLoaderVolley(this@NewsItemActivity)
            newsItemRecyclerView = findViewById(recycler_view_news_item) as RecyclerView
            Log.d("NewsItemActivity", "Recycler view asignado")
            Log.d("NewsItemActivity", "Resgitro el context menú")
            newsItemAdapter = NewsItemAdapter(this@NewsItemActivity)
            Log.d("NewsItemActivity", "Creo adaptador")
            newsItemAdapter.setOnItemClickListener(View.OnClickListener { v ->
                val position = newsItemRecyclerView.getChildAdapterPosition(v)
                Log.d("NewsItemActivity", "Posicion Elemento -"+position+"-")
                val urlNews = news.get(position).urlOfANews
                startWebNavigatorActivity(urlNews)
            })
            newsItemAdapter.setOnItemLongClickListener(View.OnLongClickListener{ v ->
                val position = newsItemRecyclerView.getChildAdapterPosition(v)
                val menu = createContextMenu(this@NewsItemActivity,v,position)
                if (menu != null) {
                    menu.show()
                }
                true
            })
            newsItemRecyclerView.adapter= newsItemAdapter
            Log.d("NewsItemActivity", "Recycler view con adaptador")
            newsItemLayoutManager = LinearLayoutManager(this@NewsItemActivity)
            Log.d("NewsItemActivity", "Creo LinearLayoutManager")
            newsItemRecyclerView.layoutManager = newsItemLayoutManager
            Log.d("NewsItemActivity", "Recycler view con LinearLayoutManager asignado")
            newsItemAdapter.notifyDataSetChanged()*/
        }
    }
}