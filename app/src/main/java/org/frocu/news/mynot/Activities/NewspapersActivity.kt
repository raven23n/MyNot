package org.frocu.news.mynot.Activities

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import org.frocu.news.mynot.Adapters.NewspapersAdapter
import org.frocu.news.mynot.POJO.NewsItem
import org.frocu.news.mynot.ParserHandlers.SAXParserHandler
import org.frocu.news.mynot.R
import org.frocu.news.mynot.Singletons.GlobalVariablesAndFuns
import org.frocu.news.mynot.Singletons.GlobalVariablesAndFuns.isNetworkConnected
import org.frocu.news.mynot.Singletons.GlobalVariablesAndFuns.positionNewspaperInCharge
import org.frocu.news.mynot.Singletons.ImageLoaderVolley
import org.frocu.news.mynot.Singletons.NewsItemList
import org.frocu.news.mynot.Singletons.NewsItemList.news
import org.frocu.news.mynot.Singletons.NewspapersList.newspapers
import org.xml.sax.SAXException
import java.io.IOException
import java.util.ArrayList
import javax.xml.parsers.ParserConfigurationException
import javax.xml.parsers.SAXParserFactory

class NewspapersActivity : AppCompatActivity() {

    lateinit var newspapersAdapter : NewspapersAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerView: RecyclerView
    init{
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_newspapers)
        Log.d("NewspapersActivity", "Entro en onCreate")
    }

    override fun onResume() {
        super.onResume()
        this@NewspapersActivity.onTitleChanged(
                GlobalVariablesAndFuns.sectionActual,
                Color.parseColor(GlobalVariablesAndFuns.colorActual)
        )
        var accessNewspapersActivity= GlobalVariablesAndFuns.checkSharedPreferencesKey(this, "accessNewspapersActivity")
        if (accessNewspapersActivity == "N") {
            var message = "En esta ventana se mostrarán los periódicos disponibles para la sección elegida. <br><br>" +
                    "Pulsa sobre un periódico para acceder a sus noticias."
            AlertDialog.Builder(this)
                    .setTitle("MyNot")
                    .setMessage(Html.fromHtml(message))
                    .setPositiveButton("OK", DialogInterface.OnClickListener(){
                        dialogInterface: DialogInterface, i: Int ->
                        fun onClick(dialog: DialogInterface, id:Int){
                            dialog.cancel()
                        }
                    })
                    .show()
            GlobalVariablesAndFuns.updateSharedPreference(this, "accessNewspapersActivity", "S")
        }
        Log.d("NewspapersActivity", "Entro en onResume")
        ImageLoaderVolley.initializeImageLoaderVolley(this@NewspapersActivity)
        recyclerView = findViewById(R.id.recycler_view_newspapers) as RecyclerView
        recyclerView.setBackgroundColor(Color.parseColor(GlobalVariablesAndFuns.colorActual))
        Log.d("NewspapersActivity", "Recycler view asignado")
        newspapersAdapter = NewspapersAdapter(this)
        Log.d("NewspapersActivity", "Creo adaptador")
        newspapersAdapter.setOnItemClickListener(View.OnClickListener { v ->
            val position : Int? = recyclerView.getChildAdapterPosition(v)
            Log.d("NewspapersActivity", "Posicion Elemento -"+position+"-")
            if(isNetworkConnected(this))
                startNewsItemActivity(position)
            else
                Toast.makeText(applicationContext,"No se detecta acceso a internet. Por favor, revise su conexión e intentelo de nuevo.", Toast.LENGTH_LONG).show()
        })
        recyclerView.adapter= newspapersAdapter
        Log.d("NewspapersActivity", "Recycler view con adaptador")
        layoutManager = LinearLayoutManager(this)
        Log.d("NewspapersActivity", "Creo LinearLayoutManager")
        recyclerView.layoutManager = layoutManager
        Log.d("NewspapersActivity", "Recycler view con LinearLayoutManager asignado")
        newspapersAdapter.notifyDataSetChanged()
    }

    fun startNewsItemActivity(position :Int?){
        if(position!=null)
            positionNewspaperInCharge = position
        AccessToNews().execute(newspapers[positionNewspaperInCharge].urlNewspaper)
    }

    inner class AccessToNews: AsyncTask<String, Void, ArrayList<NewsItem>>() {
        val mProgressDialog = ProgressDialog(this@NewspapersActivity)
        override fun onPreExecute() {
            super.onPreExecute()
            mProgressDialog.setMessage("Buscando...")
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            mProgressDialog.setCancelable(true)
            mProgressDialog.show()
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
                news.clear()
                if (mProgressDialog.isShowing) {
                    mProgressDialog.dismiss()
                }
//                Toast.makeText(applicationContext,"Error al cargar la página.", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            } catch (e: SAXException) {
                news.clear()
                if (mProgressDialog.isShowing) {
                    mProgressDialog.dismiss()
                }
//                Toast.makeText(applicationContext,"Error al transformar la página.", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            } catch (e: Exception) {
                news.clear()
                if (mProgressDialog.isShowing) {
                    mProgressDialog.dismiss()
                }
//                Toast.makeText(applicationContext,"Error al cargar la página.", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
            return news
        }

        override fun onPostExecute(news: ArrayList<NewsItem>) {
            if (mProgressDialog.isShowing) {
                mProgressDialog.dismiss()
            }
            if(news.isNotEmpty()){
                val intent = Intent(this@NewspapersActivity, NewsItemActivity::class.java)
                startActivity(intent)
            }else
                Toast.makeText(applicationContext,"No hay noticias disponibles en este periódico.", Toast.LENGTH_LONG).show()
        }
    }
}