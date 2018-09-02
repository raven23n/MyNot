package org.frocu.news.mynot.Activities

import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import kotlinx.android.synthetic.main.button_news_saved.*
import kotlinx.android.synthetic.main.content_initial.*
import org.frocu.news.mynot.Databases.NewspapersDatabase
import org.frocu.news.mynot.Databases.NewspapersDatabaseFirestore
import org.frocu.news.mynot.R
import org.frocu.news.mynot.Singletons.FirestoreInstance.initialiceFirestoreInstance
import org.frocu.news.mynot.Singletons.GlobalVariables.positionNewspaperInCharge
import org.frocu.news.mynot.Singletons.GlobalVariables.sectionActual
import org.frocu.news.mynot.Singletons.NewsItemList.news
import org.frocu.news.mynot.Singletons.NewspapersList.newspapers
import org.frocu.news.mynot.Singletons.NewsSavedDatabaseObject.initializeInstanceDatabase
import org.frocu.news.mynot.Singletons.NewsSavedDatabaseObject.instanceDatabase

class InitialActivity : AppCompatActivity()/*, NavigationView.OnNavigationItemSelectedListener*/{

    lateinit var newspapersDatabase: NewspapersDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)
    }

    override fun onResume() {
        super.onResume()
        //initializeLayout()
        initializeInstanceDatabase(this)
        initialiceFirestoreInstance()
        initializeButtons()
    }

    fun initializeButtons(){
        news_saved_background.setOnClickListener {
            openNewsSaved()
        }
        cardview_science.setOnClickListener{
            searchNewspapersInDB(resources.getString(R.string.science))
        }
        cardview_autonomous_communities.setOnClickListener{ openCCAA() }
        cardview_sports.setOnClickListener{
            searchNewspapersInDB(resources.getString(R.string.sports))
        }
        cardview_economy.setOnClickListener{
            searchNewspapersInDB(resources.getString(R.string.economy))
        }
        cardview_international.setOnClickListener{
            searchNewspapersInDB(resources.getString(R.string.international))
        }
        cardview_spain.setOnClickListener{
            searchNewspapersInDB(resources.getString(R.string.spain))
        }
        cardview_technology.setOnClickListener{
            searchNewspapersInDB(resources.getString(R.string.technology))
        }
        cardview_last_news.setOnClickListener{
            searchNewspapersInDB(resources.getString(R.string.last_news))
    }
    }

    fun openCCAA () {
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

/*    override fun onNavigationItemSelected(item: MenuItem): Boolean {
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
    }*/