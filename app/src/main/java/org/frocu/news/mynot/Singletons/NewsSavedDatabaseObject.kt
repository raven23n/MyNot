package org.frocu.news.mynot.Singletons

import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import org.frocu.news.mynot.Databases.NewsSavedDatabase
import org.frocu.news.mynot.Databases.NewsSavedDatabaseSQLite

object NewsSavedDatabaseObject {
    lateinit var instanceDatabase: NewsSavedDatabase

    fun initializeInstanceDatabase(context: Context){
        instanceDatabase = NewsSavedDatabaseSQLite(context = context)
    }
}