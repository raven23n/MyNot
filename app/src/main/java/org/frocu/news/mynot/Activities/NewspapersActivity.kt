package org.frocu.news.mynot.Activities

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.list_of_newspapers.recycler_view_newspapers
import org.frocu.news.mynot.Adapters.NewspapersAdapter
import org.frocu.news.mynot.R

class NewspapersActivity : AppCompatActivity() {

    lateinit var newspapersAdapter : NewspapersAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager
    var recyclerView: RecyclerView ?= null
    init{
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_of_newspapers)
        Log.d("NewspapersActivity", "Entro en onCreate")
    }

    override fun onResume() {
        super.onResume()
        Log.d("NewspapersActivity", "Entro en onResume")
        recyclerView = findViewById(R.id.recycler_view_newspapers) as RecyclerView?
        Log.d("NewspapersActivity", "Recycler view asignado")
        newspapersAdapter = NewspapersAdapter(this)
        Log.d("NewspapersActivity", "Creo adaptador")
        newspapersAdapter.setOnItemClickListener(View.OnClickListener { v ->
            val pos = recyclerView?.getChildAdapterPosition(v)
            Log.d("NewspapersActivity", "Posicion Elemento -"+pos+"-")
        })
        recyclerView?.adapter= newspapersAdapter
        Log.d("NewspapersActivity", "Recycler view con adaptador")
        layoutManager = LinearLayoutManager(this)
        Log.d("NewspapersActivity", "Creo LinearLayoutManager")
        recyclerView?.layoutManager = layoutManager
        Log.d("NewspapersActivity", "Recycler view con LinearLayoutManager asignado")
        newspapersAdapter.notifyDataSetChanged()
    }
}