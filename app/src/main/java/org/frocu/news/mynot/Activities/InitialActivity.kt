package org.frocu.news.mynot.Activities

import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.button_news_saved.*
import kotlinx.android.synthetic.main.content_initial.*
import org.frocu.news.mynot.Adapters.NewspapersAdapter
import org.frocu.news.mynot.Adapters.SectionAdapter
import org.frocu.news.mynot.Databases.NewspapersDatabase
import org.frocu.news.mynot.Databases.NewspapersDatabaseFirestore
import org.frocu.news.mynot.Databases.SectionDatabase
import org.frocu.news.mynot.Databases.SectionDatabaseFirestore
import org.frocu.news.mynot.R
import org.frocu.news.mynot.Singletons.FirebaseToolsInstance.initialiceFirestoreInstance
import org.frocu.news.mynot.Singletons.FirebaseToolsInstance.initialiceStorageInstance
import org.frocu.news.mynot.Singletons.GlobalVariables
import org.frocu.news.mynot.Singletons.GlobalVariables.cleanAppArrays
import org.frocu.news.mynot.Singletons.GlobalVariables.colorActual
import org.frocu.news.mynot.Singletons.GlobalVariables.positionNewspaperInCharge
import org.frocu.news.mynot.Singletons.GlobalVariables.sectionActual
import org.frocu.news.mynot.Singletons.ImageLoaderVolley
import org.frocu.news.mynot.Singletons.NewsItemList.news
import org.frocu.news.mynot.Singletons.NewspapersList.newspapers
import org.frocu.news.mynot.Singletons.NewsSavedDatabaseObject.initializeInstanceDatabase
import org.frocu.news.mynot.Singletons.NewsSavedDatabaseObject.instanceDatabase
import org.frocu.news.mynot.Singletons.SectionList.sections

class InitialActivity : AppCompatActivity()/*, NavigationView.OnNavigationItemSelectedListener*/{

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
        //initializeLayout()
        cleanAppArrays()
        initializeInstanceDatabase(this)
        initialiceFirestoreInstance()
        initialiceStorageInstance()
        searchSectionsDB()
        initializeButtons()
    }

    fun initializeButtons(){
        news_saved_background.setOnClickListener {
            openNewsSaved()
        }
    }

    fun loadSections(){
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
                    "C. Autónomas"-> openCCAA()
                    else -> searchNewspapersInDB(section)
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

    fun searchSectionsDB(){
        var sectionsListener = object:SectionDatabase.SectionsListener{
            override fun onRespuesta(endOfQuery: Boolean) {
                Log.d("onRespuesta", "Entro en Initial Sections onRespuesta")
                if (endOfQuery) {
                    for (section in sections) {
                        Log.d("Sections InitialAct ", "-" + section.sectionName + "-")
                    }
                    loadSections()
                }
            }
        }
        sectionDatabase = SectionDatabaseFirestore()
        sectionDatabase.readSections(sectionsListener = sectionsListener)
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
                    startNewspapersActivity()
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
}