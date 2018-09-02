package org.frocu.news.mynot.Activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import org.frocu.news.mynot.Adapters.NewspapersAdapter
import org.frocu.news.mynot.R
import org.frocu.news.mynot.Singletons.GlobalVariables
import org.frocu.news.mynot.Singletons.GlobalVariables.positionNewspaperInCharge
import org.frocu.news.mynot.Singletons.GlobalVariables.sectionActual
import org.frocu.news.mynot.Singletons.ImageLoaderVolley

class NewspapersActivity : AppCompatActivity() {

    lateinit var newspapersAdapter : NewspapersAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerView: RecyclerView
    init{
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_newspapers)
        Log.d("NewspapersActivity", "Entro en onCreate")
    }

    override fun onResume() {
        super.onResume()
        this@NewspapersActivity.title = sectionActual
        this@NewspapersActivity.titleColor = Color.parseColor(GlobalVariables.colorActual)
        Log.d("NewspapersActivity", "Entro en onResume")
        ImageLoaderVolley.initializeImageLoaderVolley(this@NewspapersActivity)
        recyclerView = findViewById(R.id.recycler_view_newspapers) as RecyclerView
        recyclerView.setBackgroundColor(Color.parseColor(GlobalVariables.colorActual))
        Log.d("NewspapersActivity", "Recycler view asignado")
        newspapersAdapter = NewspapersAdapter(this)
        Log.d("NewspapersActivity", "Creo adaptador")
        newspapersAdapter.setOnItemClickListener(View.OnClickListener { v ->
            val position : Int? = recyclerView.getChildAdapterPosition(v)
            Log.d("NewspapersActivity", "Posicion Elemento -"+position+"-")
            startNewsItemActivity(position)
        })
        recyclerView.adapter= newspapersAdapter
        Log.d("NewspapersActivity", "Recycler view con adaptador")
        layoutManager = LinearLayoutManager(this)
        Log.d("NewspapersActivity", "Creo LinearLayoutManager")
        recyclerView.layoutManager = layoutManager
        Log.d("NewspapersActivity", "Recycler view con LinearLayoutManager asignado")
        newspapersAdapter.notifyDataSetChanged()
    }

    fun startNewsItemActivity(position :Int?){
        val intent = Intent(this, NewsItemActivity::class.java)
        if(position!=null)
            positionNewspaperInCharge = position
        startActivity(intent)
    }
}