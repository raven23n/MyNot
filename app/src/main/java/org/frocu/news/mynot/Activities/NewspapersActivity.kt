package org.frocu.news.mynot.Activities

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.list_of_newspapers.*
import org.frocu.news.mynot.Adapters.NewspapersAdapter
import org.frocu.news.mynot.R

class NewspapersActivity : AppCompatActivity() {

    var newspapersAdapter : NewspapersAdapter
    var layoutManager: RecyclerView.LayoutManager

    init{
        newspapersAdapter = NewspapersAdapter(this)
        layoutManager = LinearLayoutManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.list_of_newspapers)
    }

    override fun onResume() {
        super.onResume()
        recycler_view_newspapers.adapter = newspapersAdapter
        recycler_view_newspapers.layoutManager = layoutManager
        newspapersAdapter.notifyDataSetChanged()
    }
}