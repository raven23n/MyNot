package org.frocu.news.mynot.Activities

import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.button_news_saved.*
import org.frocu.news.mynot.Adapters.SectionAdapter
import org.frocu.news.mynot.Databases.*
import org.frocu.news.mynot.R
import org.frocu.news.mynot.Singletons.FirebaseToolsInstance.initialiceFirestoreInstance
import org.frocu.news.mynot.Singletons.GlobalVariablesAndFuns.checkSharedPreferencesKey
import org.frocu.news.mynot.Singletons.GlobalVariablesAndFuns.cleanAppArrays
import org.frocu.news.mynot.Singletons.GlobalVariablesAndFuns.colorActual
import org.frocu.news.mynot.Singletons.GlobalVariablesAndFuns.isNetworkConnected
import org.frocu.news.mynot.Singletons.GlobalVariablesAndFuns.positionNewspaperInCharge
import org.frocu.news.mynot.Singletons.GlobalVariablesAndFuns.sectionActual
import org.frocu.news.mynot.Singletons.GlobalVariablesAndFuns.updateSharedPreference
import org.frocu.news.mynot.Singletons.NewsItemList.news
import org.frocu.news.mynot.Singletons.NewspapersList.newspapers
import org.frocu.news.mynot.Singletons.NewsSavedDatabaseObject.initializeInstanceDatabase
import org.frocu.news.mynot.Singletons.NewsSavedDatabaseObject.instanceDatabase
import org.frocu.news.mynot.Singletons.SectionList.orderByNumberOfAccessToSection
import org.frocu.news.mynot.Singletons.SectionList.sections

class InitialActivity : AppCompatActivity(){

    lateinit var newspapersDatabase: NewspapersDatabase
    lateinit var sectionDatabase: SectionDatabase

    lateinit var sectionAdapter: SectionAdapter
    lateinit var layoutManagerSections: RecyclerView.LayoutManager
    lateinit var recyclerViewSections: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)
    }

    override fun onResume() {
        super.onResume()
        cleanAppArrays()
        initializeInstanceDatabase(this)
        initialiceFirestoreInstance()
        searchSectionsDB()
        initializeButtons()
    }

    fun searchSectionsDB(){
        var accessInitialActivity = checkSharedPreferencesKey(this,"accessInitialActivity")
        if (accessInitialActivity == "N") {
            var sectionDatabaseArray = SectionDatabaseArray()
            sectionDatabaseArray.searchSection("S")
            sectionDatabase = SectionDatabaseSQLite(this)
            sectionDatabase.saveSections("S")
            var message = "En esta ventana se mostrarán las secciones disponibles. <br><br>" +
                    "Conforme accedas a ellas, se irán reordenando para mostrar las que más uses en los primeros lugares."
            AlertDialog.Builder(this)
                    .setTitle("MyNot")
                    .setMessage(Html.fromHtml(message))
                    .setPositiveButton("OK",DialogInterface.OnClickListener(){
                        dialogInterface: DialogInterface, i: Int ->
                        fun onClick(dialog:DialogInterface, id:Int){
                            dialog.cancel()
                        }
                    })
                    .show()
            updateSharedPreference(this,"accessInitialActivity","S")
        }else{
            sectionDatabase = SectionDatabaseSQLite(this)
            sectionDatabase.searchSection("S")
        }
        loadSections()
    }

    fun loadSections(){
        orderByNumberOfAccessToSection()
        Log.d("LoadSections", "Entro en onResume")
        recyclerViewSections = findViewById(R.id.recycler_view_sections) as RecyclerView
        Log.d("LoadSections", "Recycler view asignado")
        sectionAdapter = SectionAdapter(this)
        Log.d("LoadSections", "Creo adaptador")
        sectionAdapter.setOnItemClickListener(View.OnClickListener { v ->
            val position : Int? = recyclerViewSections.getChildAdapterPosition(v)
            Log.d("LoadSections", "Posicion Elemento -"+position+"-")
            if (position != null) {
                var section = sections.get(position).sectionName
                colorActual = sections.get(position).sectionColour
                when(section){
                    "C. Autónomas"-> {
                        sectionDatabase.updateCountSections(sections.get(position),
                                "S",
                                position)
                        openCCAA()
                    }
                    else -> {
                        if(isNetworkConnected(this)) {
                            sectionDatabase.updateCountSections(sections.get(position),
                                    "S",
                                    position)
                            searchNewspapersInDB(section)
                        }else{
                            Toast.makeText(this,"No se detecta acceso a internet. Por favor, revise su conexión a internet y vuelva a intentarlo.", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        })
        recyclerViewSections.adapter= sectionAdapter
        Log.d("LoadSections", "Recycler view con adaptador")
        layoutManagerSections = GridLayoutManager(this,2)
        Log.d("LoadSections", "Creo GridLayoutManager")
        recyclerViewSections.layoutManager = layoutManagerSections
        Log.d("LoadSections", "Recycler view con GridLayoutManager asignado")
        sectionAdapter.notifyDataSetChanged()
    }


    fun openCCAA () {
        sectionActual = "C. Autónomas"
        var intent = Intent(this, AutonomousCommunitiesActivity::class.java)
        startActivity (intent)
    }

    fun searchNewspapersInDB(section : String){
        sectionActual = section
        var newspapersListener = object:NewspapersDatabase.NewspapersListener{
            override fun onRespuesta(endOfQuery: Boolean) {
                Log.d("onRespuesta", "Entro en Initial onRespuesta")
                if (endOfQuery) {
                    for (newsp in newspapers) {
                        Log.d("Periodicos InitialAct ", "-" + newsp.nameNewspaper + "-")
                    }
                    Log.d("onRespuesta", "Entro en true endOfQuery")
                    startNewspapersActivity()
                }else{
                    Toast.makeText(this@InitialActivity,"No hay periódicos disponibles para esta sección.", Toast.LENGTH_LONG).show()
                    Log.d("onRespuesta", "Entro en false endOfQuery")
                }
            }
        }
        newspapersDatabase = NewspapersDatabaseFirestore(section)
        newspapersDatabase.readNewspapers(newspapersListener = newspapersListener)
    }

    fun startNewspapersActivity(){
        val intent = Intent(this, NewspapersActivity::class.java)
        startActivity(intent)
    }


    fun initializeButtons(){
        news_saved_background.setOnClickListener {
            openNewsSaved()
        }
    }

    fun openNewsSaved(){
        news = instanceDatabase.obtainNewsItemsSaved()
        positionNewspaperInCharge = -1
        if(news.size<=0){
            AlertDialog.Builder(this)
                    .setTitle("MyNot")
                    .setMessage("No tienes guardada ninguna noticia.")
                    .setPositiveButton("OK",DialogInterface.OnClickListener(){
                        dialogInterface: DialogInterface, i: Int ->
                        fun onClick(dialog:DialogInterface, id:Int){
                            dialog.cancel()
                        }
                    })
                    .show()
        }else{
            val intent = Intent(this, NewsItemActivity::class.java)
            startActivity(intent)
        }
    }

}