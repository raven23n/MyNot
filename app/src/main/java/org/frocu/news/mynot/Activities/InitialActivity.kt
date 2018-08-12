package org.frocu.news.mynot.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.CardView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import org.frocu.news.mynot.Databases.NewspapersDatabase
import org.frocu.news.mynot.Databases.NewspapersDatabaseFirestore
import org.frocu.news.mynot.Databases.SectionDatabase
import org.frocu.news.mynot.Databases.SectionDatabaseFirestore
import org.frocu.news.mynot.POJO.Newspaper
import org.frocu.news.mynot.R

class InitialActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    var sectionDatabase: SectionDatabase = SectionDatabaseFirestore()
    lateinit var newspapersDatabase: NewspapersDatabase
    lateinit var cardViewAutonomousCommunities: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)
        var section : String = "Ciencia"
        newspapersDatabase = NewspapersDatabaseFirestore(section)
//        var newspapersListener : NewspapersDatabase.NewspapersListener = NewspapersDatabase.NewspapersListener()
        sectionDatabase.readSections()
        newspapersDatabase.readNewspapers()
    }

    override fun onResume() {
        super.onResume()
        cardViewAutonomousCommunities = findViewById(R.id.cardview_autonomous_communities)
        cardViewAutonomousCommunities.setOnClickListener{
            openCCAA()
        }

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
    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun openCCAA () {
        var intent : Intent = Intent(this, AutonomousCommunitiesActivity::class.java)
        startActivity (intent)
    }
}
