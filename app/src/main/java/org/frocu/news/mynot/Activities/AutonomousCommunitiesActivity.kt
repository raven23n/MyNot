package org.frocu.news.mynot.Activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.content_ccaa.*
import org.frocu.news.mynot.Adapters.CCAAAdapter
import org.frocu.news.mynot.Adapters.SectionAdapter
import org.frocu.news.mynot.Databases.NewspapersDatabase
import org.frocu.news.mynot.Databases.NewspapersDatabaseFirestore
import org.frocu.news.mynot.Databases.SectionDatabase
import org.frocu.news.mynot.Databases.SectionDatabaseFirestore
import org.frocu.news.mynot.R
import org.frocu.news.mynot.Singletons.CCAAList.ccaaList
import org.frocu.news.mynot.Singletons.GlobalVariables
import org.frocu.news.mynot.Singletons.ImageLoaderVolley
import org.frocu.news.mynot.Singletons.NewspapersList.newspapers
import org.frocu.news.mynot.Singletons.SectionList

class AutonomousCommunitiesActivity : AppCompatActivity() {

    lateinit var newspapersDatabase: NewspapersDatabase
    lateinit var sectionDatabase: SectionDatabase

    lateinit var ccaaAdapter: CCAAAdapter
    lateinit var layoutManagerSections: RecyclerView.LayoutManager
    lateinit var recyclerViewSections: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_ccaa)
    }

    override fun onResume() {
        super.onResume()
        initializeLayout()
        searchCCAADB()
        initializeButtons()
    }

    fun initializeLayout(){
        this@AutonomousCommunitiesActivity.title = GlobalVariables.sectionActual
        this@AutonomousCommunitiesActivity.titleColor = Color.parseColor(GlobalVariables.colorActual)
    }

    fun initializeButtons(){
    }

    fun loadCCAA(){
        Log.d("LoadSections", "Entro en onResume")
        recyclerViewSections = findViewById(R.id.recycler_view_ccaa) as RecyclerView
        recyclerViewSections.setBackgroundColor(Color.parseColor(GlobalVariables.colorActual))
        Log.d("LoadSections", "Recycler view asignado")
        ccaaAdapter = CCAAAdapter(this)
        Log.d("LoadSections", "Creo adaptador")
        ccaaAdapter.setOnItemClickListener(View.OnClickListener { v ->
            val position : Int? = recyclerViewSections.getChildAdapterPosition(v)
            Log.d("LoadSections", "Posicion Elemento -"+position+"-")
            if (position != null) {
                var section = ccaaList.get(position).sectionName
                searchCCAANewspapersInDB(section)
            }
        })
        recyclerViewSections.adapter= ccaaAdapter
        Log.d("LoadSections", "Recycler view con adaptador")
        layoutManagerSections = GridLayoutManager(this,2)
        Log.d("LoadSections", "Creo GridLayoutManager")
        recyclerViewSections.layoutManager = layoutManagerSections
        Log.d("LoadSections", "Recycler view con GridLayoutManager asignado")
        ccaaAdapter.notifyDataSetChanged()
    }

    fun searchCCAADB(){
        var sectionsListener = object: SectionDatabase.SectionsListener{
            override fun onRespuesta(endOfQuery: Boolean) {
                Log.d("onRespuesta", "Entro en Initial Sections onRespuesta")
                if (endOfQuery) {
                    for (section in ccaaList) {
                        Log.d("CCAA InitialAct ", "-" +  section.sectionName + "-")
                    }
                    loadCCAA()
                }
            }
        }
        sectionDatabase = SectionDatabaseFirestore()
        sectionDatabase.readCCAA(sectionsListener = sectionsListener)
    }

    fun searchCCAANewspapersInDB(autonomousCommunitySection : String){
        GlobalVariables.sectionActual = autonomousCommunitySection
        var newspapersListener = object:NewspapersDatabase.NewspapersListener{
            override fun onRespuesta(endOfQuery: Boolean) {
                Log.d("onRespuesta", "Entro en CCAA onRespuesta")
                if (endOfQuery) {
                    for (newsp in newspapers) {
                        Log.d("Periodicos InitialAct ", "-" + newsp.nameNewspaper + "-")
                    }
                    startNewspapersActivity()
                }
            }
        }
        newspapersDatabase = NewspapersDatabaseFirestore(resources.getString(R.string.autonomous_communities))
        var countrySection = resources.getString(R.string.country_db)
        newspapersDatabase.readCCAANewspapers(country = countrySection,
                autonomous_communities = autonomousCommunitySection,
                newspapersListener = newspapersListener)
    }


    fun startNewspapersActivity(){
        val intent = Intent(this, NewspapersActivity::class.java)
        startActivity(intent)
    }
}