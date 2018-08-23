package org.frocu.news.mynot.Activities

import android.app.ProgressDialog
import android.app.ProgressDialog.show
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.sax.EndElementListener
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.content_initial.*
import org.frocu.news.mynot.Databases.NewspapersDatabase
import org.frocu.news.mynot.Databases.NewspapersDatabaseFirestore
import org.frocu.news.mynot.Databases.SectionDatabase
import org.frocu.news.mynot.Databases.SectionDatabaseFirestore
import org.frocu.news.mynot.POJO.Newspaper
import org.frocu.news.mynot.R
import org.frocu.news.mynot.Singletons.NewspapersList.newspapers
import java.lang.Thread.sleep

class InitialActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    //var sectionDatabase: SectionDatabase = SectionDatabaseFirestore()
    lateinit var newspapersDatabase: NewspapersDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)
    }

    override fun onResume() {
        super.onResume()
        initializeLayout()
        initializeButtons()
        //sectionDatabase.readSections()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun initializeLayout(){
        val toolbar = findViewById(R.id.initial_toolbar) as Toolbar
        setSupportActionBar(toolbar)
        // Navigation Drawer

        var drawer_layout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        var toggle = ActionBarDrawerToggle(this,
                drawer_layout, toolbar, R.string.drawer_open, R.string.drawer_close)

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        val nav_view = findViewById<View>(R.id.nav_view) as NavigationView
        nav_view.setNavigationItemSelectedListener(this)
    }

    fun initializeButtons(){
        cardview_science.setOnClickListener{
            searchNewspapersInDB(resources.getString(R.string.science_db))
        }
        cardview_autonomous_communities.setOnClickListener{ openCCAA() }
        cardview_sports.setOnClickListener{
            searchNewspapersInDB(resources.getString(R.string.sports_db))
        }
        cardview_economy.setOnClickListener{
            searchNewspapersInDB(resources.getString(R.string.economy_db))
        }
        cardview_international.setOnClickListener{
            searchNewspapersInDB(resources.getString(R.string.international_db))
        }
        cardview_spain.setOnClickListener{
            searchNewspapersInDB(resources.getString(R.string.spain_db))
        }
        cardview_technology.setOnClickListener{
            searchNewspapersInDB(resources.getString(R.string.technology_db))
        }
        cardview_last_news.setOnClickListener{
            searchNewspapersInDB(resources.getString(R.string.last_news_db))
        }
    }

    fun openCCAA () {
        var intent : Intent = Intent(this, AutonomousCommunitiesActivity::class.java)
        startActivity (intent)
    }

    fun searchNewspapersInDB(section : String){
        //changeLoaded(false)
        var newspapersListener = object:NewspapersDatabase.NewspapersListener{
            override fun onRespuesta(endOfQuery: Boolean) {
                Log.d("onRespuesta", "Entro en Initial onRespuesta")
                //Log.d("Nº periodicos :", "-"+newspapersList.size.toString()+"-")
                if (endOfQuery) {
                    for (newsp in newspapers) {
                        Log.d("Periodicos InitialAct ", "-" + newsp.nameNewspaper + "-")
                    }
//                    changeLoaded(true)
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
