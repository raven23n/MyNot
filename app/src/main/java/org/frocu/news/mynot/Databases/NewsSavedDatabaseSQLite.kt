package org.frocu.news.mynot.Databases

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import org.frocu.news.mynot.POJO.NewsItem
import java.util.ArrayList

class NewsSavedDatabaseSQLite(
        val context: Context?,
        name: String = "news",
        factory: SQLiteDatabase.CursorFactory? = null,
        version: Int = 1
)
    : NewsSavedDatabase , SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE news (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "headline TEXT, image TEXT, url TEXT, date TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase,
                           oldVersion: Int, newVersion: Int) {
    }

    override fun saveNewsItem(newsItem: NewsItem) {
        val db = getWritableDatabase()
        val headline = newsItem.headlineOfANews
        val image = newsItem.imageOfANews
        val url = newsItem.urlOfANews
        val date = newsItem.dateOfANews
        if (!searchNewsItemsSaved(db,newsItem)) {
            db.execSQL("INSERT INTO news VALUES ( null, '"
                    + headline + "', '"
                    + image + "', '"
                    + url + "', '"
                    + date + "')")
            Log.d("Guardado de noticias: ","Noticia guardada.")
        }
        Log.d("Guardado de noticias: ","Fin guardar noticia.")
        db.close()
    }

    override fun obtainNewsItemsSaved(): ArrayList<NewsItem> {
        val result = ArrayList<NewsItem>()
        val db = getReadableDatabase()
        val CAMPOS = arrayOf("id","headline", "image", "url", "date")
        val cursor = db.query("news", CAMPOS, null, null, null, null, "id ASC", null)
        while (cursor.moveToNext()) {
            val headline = cursor.getString(1)
            val image = cursor.getString(2)
            val url = cursor.getString(3)
            val date = cursor.getString(4)
            val newsItemRecovered = NewsItem(
                    headlineOfANews = headline,
                    imageOfANews = image,
                    urlOfANews = url,
                    dateOfANews = date)
            result.add(newsItemRecovered)
        }
        cursor.close()
        db.close()
        return result
    }

    override fun deleteNewsItem(newsItem: NewsItem) {
        val db = getWritableDatabase()
        if (searchNewsItemsSaved(db,newsItem)) {
            db.execSQL("DELETE FROM news WHERE headline = '" + newsItem.headlineOfANews
                    + "' AND image = '" + newsItem.imageOfANews
                    + "' AND url = '" + newsItem.urlOfANews
                    + "' AND date = '" + newsItem.dateOfANews + "'"
            )
        }
        db.close()
    }

    fun searchNewsItemsSaved(db: SQLiteDatabase,newsItem: NewsItem): Boolean {
        val cursor = db.rawQuery("SELECT * FROM news" +
                " WHERE headline = '" + newsItem.headlineOfANews
                + "' AND image = '" + newsItem.imageOfANews
                + "' AND url = '" + newsItem.urlOfANews
                + "' AND date = '" + newsItem.dateOfANews + "'", null);
        val itemsFound : Boolean = cursor.count > 0
        Log.d("Buscar un item: ","Registros encontrados cursor.count: -"+ cursor.count +"-")
        Log.d("Buscar un item: ","Registros encontrados itemFound: -"+ itemsFound +"-")
        cursor.close()
        return itemsFound
    }
}