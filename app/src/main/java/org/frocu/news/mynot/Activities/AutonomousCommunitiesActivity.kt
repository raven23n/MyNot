package org.frocu.news.mynot.Activities

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import org.frocu.news.mynot.Adapters.CCAAAdapter
import org.frocu.news.mynot.Databases.*
import org.frocu.news.mynot.R
import org.frocu.news.mynot.Singletons.CCAAList.ccaaList
import org.frocu.news.mynot.Singletons.CCAAList.orderByNumberOfAccessToCCAA
import org.frocu.news.mynot.Singletons.GlobalVariablesAndFuns
import org.frocu.news.mynot.Singletons.GlobalVariablesAndFuns.isNetworkConnected
import org.frocu.news.mynot.Singletons.GlobalVariablesAndFuns.updateSharedPreference
import org.frocu.news.mynot.Singletons.NewspapersList.newspapers

class AutonomousCommunitiesActivity : AppCompatActivity() {

    lateinit var newspapersDatabase: NewspapersDatabase
    lateinit var sectionDatabase: SectionDatabase

    lateinit var ccaaAdapter: CCAAAdapter
    lateinit var layoutManagerSections: RecyclerView.LayoutManager
    lateinit var recyclerViewSections: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ccaa)
    }

    override fun onResume() {
        super.onResume()
        initializeLayout()
        searchCCAADB()
        initializeButtons()
    }

    fun initializeLayout(){
        //this@AutonomousCommunitiesActivity.title = GlobalVariables.sectionActual
        //this@AutonomousCommunitiesActivity.titleColor = Color.parseColor(GlobalVariables.colorActual)
        this@AutonomousCommunitiesActivity.onTitleChanged(
                GlobalVariablesAndFuns.sectionActual,
                Color.parseColor(GlobalVariablesAndFuns.colorActual)
        )
        ccaaList.clear()
    }

    fun initializeButtons(){
    }

    fun searchCCAADB(){
        var accessAutonomousCommunitiesActivity= GlobalVariablesAndFuns.checkSharedPreferencesKey(this, "accessAutonomousCommunitiesActivity")
        Log.d("LoadSections", "Entro en accessAutonomousCommunitiesActivity -"+accessAutonomousCommunitiesActivity+"-")
        if (accessAutonomousCommunitiesActivity == "N") {
            var sectionDatabaseArray = SectionDatabaseArray()
            sectionDatabaseArray.searchSection("C")
            sectionDatabase = SectionDatabaseSQLite(this)
            sectionDatabase.saveSections("C")
            var message = "En esta ventana se mostrarán las distintas Comunidades Autónomas de España. <br><br>" +
                    "Pulsa sobre la comunidad de la que deseas conocer su actualidad. <br><br>" +
                    "Al igual que en las secciones principales, éstas se irán reordenando para mostrar las que más uses en los primeros lugares."
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
            updateSharedPreference(this, "accessAutonomousCommunitiesActivity", "S")
        }else{
            sectionDatabase = SectionDatabaseSQLite(this)
            sectionDatabase.searchSection("C")
        }
        loadCCAA()
    }

    fun loadCCAA(){
        orderByNumberOfAccessToCCAA()
        Log.d("LoadSections", "Entro en onResume")
        recyclerViewSections = findViewById(R.id.recycler_view_ccaa) as RecyclerView
        recyclerViewSections.setBackgroundColor(Color.parseColor(GlobalVariablesAndFuns.colorActual))
        Log.d("LoadSections", "Recycler view asignado")
        ccaaAdapter = CCAAAdapter(this)
        Log.d("LoadSections", "Creo adaptador")
        ccaaAdapter.setOnItemClickListener(View.OnClickListener { v ->
            val position : Int? = recyclerViewSections.getChildAdapterPosition(v)
            Log.d("LoadSections", "Posicion Elemento -"+position+"-")
            if (position != null) {
                if(isNetworkConnected(this)) {
                    var section = ccaaList.get(position).sectionName
                    sectionDatabase.updateCountSections(ccaaList.get(position),
                            "C",
                            position)
                    searchCCAANewspapersInDB(section)
                }else{
                    Toast.makeText(this,"No se detecta acceso a internet. Por favor, revise su conexión a internet y vuelva a intentarlo.", Toast.LENGTH_LONG).show()
                }
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

    fun searchCCAANewspapersInDB(autonomousCommunitySection : String){
        GlobalVariablesAndFuns.sectionActual = autonomousCommunitySection
        var newspapersListener = object:NewspapersDatabase.NewspapersListener{
            override fun onRespuesta(endOfQuery: Boolean) {
                Log.d("onRespuesta", "Entro en CCAA onRespuesta")
                if (endOfQuery) {
                    for (newsp in newspapers) {
                        Log.d("Periodicos InitialAct ", "-" + newsp.nameNewspaper + "-")
                    }
                    Log.d("onRespuesta", "Entro en true endOfQuery")
                    startNewspapersActivity()
                }else{
                    Toast.makeText(this@AutonomousCommunitiesActivity,"No hsy periódicos disponibles para esta sección.", Toast.LENGTH_LONG).show()
                    Log.d("onRespuesta", "Entro en false endOfQuery")
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