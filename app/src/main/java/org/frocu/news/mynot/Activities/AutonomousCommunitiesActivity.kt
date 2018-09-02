package org.frocu.news.mynot.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.content_ccaa.*
import org.frocu.news.mynot.Databases.NewspapersDatabase
import org.frocu.news.mynot.Databases.NewspapersDatabaseFirestore
import org.frocu.news.mynot.R
import org.frocu.news.mynot.Singletons.NewspapersList.newspapers

class AutonomousCommunitiesActivity : AppCompatActivity() {

    lateinit var newspapersDatabase: NewspapersDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ccaa)
    }

    override fun onResume() {
        super.onResume()
        initializeLayout()
        initializeButtons()
    }

    fun initializeLayout(){
    }

    fun initializeButtons(){
        cardview_andalucia.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.andalucia)) }
        cardview_aragon.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.aragon)) }
        cardview_asturias.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.asturias)) }
        cardview_cantabria.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.cantabria)) }
        cardview_castillalamancha.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.castillalamancha)) }
        cardview_castillaleon.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.castillaleon)) }
        cardview_catalunya.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.catalunya)) }
        cardview_ceutamelilla.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.ceutamelilla)) }
        cardview_comunidadvalenciana.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.comunidadvalenciana)) }
        cardview_extremadura.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.extremadura)) }
        cardview_galicia.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.galicia)) }
        cardview_islasbaleares.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.islasbaleares)) }
        cardview_islascanarias.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.islascanarias)) }
        cardview_larioja.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.larioja)) }
        cardview_madrid.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.madrid)) }
        cardview_murcia.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.murcia)) }
        cardview_navarra.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.navarra)) }
        cardview_paisvasco.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.paisvasco)) }
    }

    fun searchCCAANewspapersInDB(autonomousCommunitySection : String){
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
        newspapersDatabase = NewspapersDatabaseFirestore(resources.getString(R.string.autonomous_communities_db))
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