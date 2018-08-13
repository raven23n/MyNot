package org.frocu.news.mynot.Activities

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.Toolbar
import android.view.View
import kotlinx.android.synthetic.main.content_ccaa.*
import org.frocu.news.mynot.R

class AutonomousCommunitiesActivity : AppCompatActivity() {

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
        cardview_andalucia.setOnClickListener{}
        cardview_aragon.setOnClickListener{}
        cardview_asturias.setOnClickListener{}
        cardview_cantabria.setOnClickListener{}
        cardview_castillalamancha.setOnClickListener{}
        cardview_castillaleon.setOnClickListener{}
        cardview_catalunya.setOnClickListener{}
        cardview_ceutamelilla.setOnClickListener{}
        cardview_comunidadvalenciana.setOnClickListener{}
        cardview_extremadura.setOnClickListener{}
        cardview_galicia.setOnClickListener{}
        cardview_islasbaleares.setOnClickListener{}
        cardview_islascanarias.setOnClickListener{}
        cardview_larioja.setOnClickListener{}
        cardview_madrid.setOnClickListener{}
        cardview_murcia.setOnClickListener{}
        cardview_navarra.setOnClickListener{}
        cardview_paisvasco.setOnClickListener{}
    }
}