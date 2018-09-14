package org.frocu.news.mynot.Activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import org.frocu.news.mynot.Adapters.NewsItemAdapter
import org.frocu.news.mynot.Singletons.NewsItemList.news
import org.frocu.news.mynot.R
import org.frocu.news.mynot.R.id.recycler_view_news_item
import org.frocu.news.mynot.Singletons.ImageLoaderVolley.initializeImageLoaderVolley
import org.frocu.news.mynot.Singletons.GlobalVariablesAndFuns.positionNewspaperInCharge
import org.frocu.news.mynot.Singletons.GlobalVariablesAndFuns.urlNewsItemActual

import org.frocu.news.mynot.Singletons.LongClickContextMenu.createContextMenu
import android.text.Html
import android.widget.Toast
import org.frocu.news.mynot.Singletons.GlobalVariablesAndFuns
import org.frocu.news.mynot.Singletons.GlobalVariablesAndFuns.colorActual
import org.frocu.news.mynot.Singletons.GlobalVariablesAndFuns.isNetworkConnected


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
            savedNewsItemActivity()
        }else{
            newsInNet()
        }
        Log.d("NewsItemActivity", "Entro en onResume")
        Log.d("NewsItemActivity", "positionNewspaperInCharge: -" + positionNewspaperInCharge +"-")
    }

    fun savedNewsItemActivity(){
        var accessSavedNewsItemActivity= GlobalVariablesAndFuns.checkSharedPreferencesKey(this, "accessSavedNewsItemActivity")
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
            GlobalVariablesAndFuns.updateSharedPreference(this, "accessSavedNewsItemActivity", "S")
        }
        chargeNewsItemActivity()
    }

    fun newsInNet(){
        var accessNewsItemActivity= GlobalVariablesAndFuns.checkSharedPreferencesKey(this, "accessNewsItemActivity")
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
            GlobalVariablesAndFuns.updateSharedPreference(this, "accessNewsItemActivity", "S")
        }
        chargeNewsItemActivity()
    }

    fun chargeNewsItemActivity(){
        initializeImageLoaderVolley(this@NewsItemActivity)
        newsItemRecyclerView = findViewById(recycler_view_news_item) as RecyclerView
        //newsItemRecyclerView.setBackgroundColor(Color.parseColor(GlobalVariables.colorActual))
        Log.d("NewsItemActivity", "Recycler view asignado")
        Log.d("NewsItemActivity", "colorActual: -"+ colorActual + "-")
        Log.d("NewsItemActivity", "Resgitro el context menú")
        newsItemRecyclerView.addItemDecoration(DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL))
        newsItemAdapter = NewsItemAdapter(this@NewsItemActivity)
        Log.d("NewsItemActivity", "Creo adaptador")
        newsItemAdapter.setOnItemClickListener(View.OnClickListener { v ->
            val position = newsItemRecyclerView.getChildAdapterPosition(v)
            Log.d("NewsItemActivity", "Posicion Elemento -" + position + "-")
            val urlNews = news.get(position).urlOfANews
            Log.d("NewsItemActivity", "urlNews -" + urlNews + "-")
            if(isNetworkConnected(this)){
                startWebNavigatorActivity(urlNews)
            }else{
                Toast.makeText(this,"No se detecta acceso a internet. Por favor, revise su conexión a internet y vuelva a intentarlo.", Toast.LENGTH_LONG).show()
            }

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

}