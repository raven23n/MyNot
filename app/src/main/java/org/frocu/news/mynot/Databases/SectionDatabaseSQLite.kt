package org.frocu.news.mynot.Databases

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import org.frocu.news.mynot.POJO.NewsItem
import org.frocu.news.mynot.POJO.Section
import org.frocu.news.mynot.Singletons.CCAAList.ccaaList
import org.frocu.news.mynot.Singletons.SectionList.sections
import java.util.ArrayList
import kotlin.text.Typography.section


class SectionDatabaseSQLite(
        val context: Context,
        name: String = "sections",
        factory: SQLiteDatabase.CursorFactory? = null,
        version: Int = 1
)
    : SectionDatabase , SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE sections (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "sectionType TEXT, sectionName TEXT, colorSection TEXT, sectionCount INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase,
                           oldVersion: Int, newVersion: Int) {
    }

    override fun searchSection(sectionType : String) {
        val db = getReadableDatabase()
        val cursor = db.rawQuery("SELECT * FROM sections" +
                " WHERE sectionType = '" + sectionType + "'", null);
        while(cursor.moveToNext()){
            val sectionName = cursor.getString(2)
            val colorSection = cursor.getString(3)
            val sectionCount = cursor.getInt(4)
            val section = Section(
                    sectionName = sectionName,
                    sectionColour = colorSection,
                    numberOfAccessToSection = sectionCount)
            when(sectionType) {
                "S" -> sections.add(section)
                "C" -> ccaaList.add(section)
            }
        }
        Log.d("Guardado de noticias: ","Fin guardar noticia.")
        cursor.close()
        db.close()
    }

    override fun saveSections(sectionType : String) {
        val db = getWritableDatabase()
        when(sectionType) {
            "S" -> {
                for (section in sections) {
                    val sectionType = sectionType
                    val sectionName = section.sectionName
                    val colorSection = section.sectionColour
                    val sectionCount = 0
                    db.execSQL("INSERT INTO sections VALUES ( null, '"
                            + sectionType + "', '"
                            + sectionName  + "', '"
                            + colorSection + "', '"
                            + sectionCount  + "')")
                    Log.d("Guardado de secciones: ", "Noticia guardada.")
                }
            }
            "C" -> {
                for (ccaa in ccaaList) {
                    val sectionName = ccaa.sectionName
                    val colorSection = ccaa.sectionColour
                    val sectionCount = 0
                    val sectionType = sectionType
                    db.execSQL("INSERT INTO sections VALUES ( null, '"
                            + sectionType + "', '"
                            + sectionName  + "', '"
                            + colorSection + "', '"
                            + sectionCount  + "')")
                    Log.d("Guardado de secciones: ", "Noticia guardada.")
                }
            }
        }
        Log.d("Guardado de noticias: ","Fin guardar noticia.")
        db.close()
    }

    override fun updateCountSections(section : Section, sectionType: String, position: Int) {
        val db = writableDatabase
        Log.d("updateSectionsSQL", "contador ANTES: -" + section.numberOfAccessToSection + "-")
        section.numberOfAccessToSection++
        db.execSQL("UPDATE sections SET sectionCount = '" + section.numberOfAccessToSection
                + "' WHERE sectionType = '" +  sectionType
                + "' AND sectionName = '" + section.sectionName + "'"
        )
        Log.d("updateSectionsSQL", "contador DESPUES: -" + section.numberOfAccessToSection + "-")
        when(sectionType) {
            "S" -> {
                sections[position].numberOfAccessToSection ++
            }
            "C" -> {
                ccaaList[position].numberOfAccessToSection ++
            }
        }
        db.close()
    }
}