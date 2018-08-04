package org.frocu.news.mynot.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.frocu.news.mynot.Databases.SectionDatabase
import org.frocu.news.mynot.Databases.SectionDatabaseFirestore
import org.frocu.news.mynot.R

class InitialActivity : AppCompatActivity() {
    var sectionDatabase: SectionDatabase = SectionDatabaseFirestore()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)
//        sectionDatabase.readSections()
    }
}
