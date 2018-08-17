package org.frocu.news.mynot.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.content_ccaa.*
import org.frocu.news.mynot.Databases.NewspapersDatabase
import org.frocu.news.mynot.Databases.NewspapersDatabaseFirestore
import org.frocu.news.mynot.POJO.Newspaper
import org.frocu.news.mynot.R

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
        cardview_andalucia.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.andalucia_db)) }
        cardview_aragon.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.aragon_db)) }
        cardview_asturias.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.asturias_db)) }
        cardview_cantabria.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.cantabria_db)) }
        cardview_castillalamancha.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.castillalamancha_db)) }
        cardview_castillaleon.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.castillaleon_db)) }
        cardview_catalunya.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.catalunya)) }
        cardview_ceutamelilla.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.ceutamelilla_db)) }
        cardview_comunidadvalenciana.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.comunidadvalenciana_db)) }
        cardview_extremadura.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.extremadura_db)) }
        cardview_galicia.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.galicia_db)) }
        cardview_islasbaleares.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.islasbaleares_db)) }
        cardview_islascanarias.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.islascanarias_db)) }
        cardview_larioja.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.larioja_db)) }
        cardview_madrid.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.madrid_db)) }
        cardview_murcia.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.murcia_db)) }
        cardview_navarra.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.navarra_db)) }
        cardview_paisvasco.setOnClickListener { searchCCAANewspapersInDB(resources.getString(R.string.paisvasco_db)) }
    }

    fun searchCCAANewspapersInDB(autonomousCommunitySection : String){
        newspapersDatabase = NewspapersDatabaseFirestore(resources.getString(R.string.autonomous_communities_db))
        var countrySection = resources.getString(R.string.country_db)
        var newspapersListener = object: NewspapersDatabase.NewspapersListener{
            override fun onRespuesta(newspapersList: ArrayList<Newspaper>) {
                Log.d("Nº periodicos :", "-"+newspapersList.size.toString()+"-")
                for (newsp in newspapersList){
                    Log.d("Periodicos InitialAct ", "-"+newsp.nameNewspaper +"-")
                }
            }
        }

        newspapersDatabase.readCCAANewspapers(country = countrySection,
                autonomous_communities = autonomousCommunitySection,
                newspapersListener = newspapersListener)
    }
}