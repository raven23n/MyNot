package org.frocu.news.mynot.Singletons

import android.content.Context
import org.frocu.news.mynot.Singletons.CCAAList.ccaaList
import org.frocu.news.mynot.Singletons.NewsItemList.news
import org.frocu.news.mynot.Singletons.NewspapersList.newspapers
import org.frocu.news.mynot.Singletons.SectionList.sections
import android.content.SharedPreferences

object GlobalVariables {
    var positionNewspaperInCharge : Int = -1
    var urlNewsItemActual : String = ""
    var sectionActual : String = ""
    var colorActual : String = "#FFEC97"

    fun cleanAppArrays(){
        sections.clear()
        ccaaList.clear()
        newspapers.clear()
        news.clear()
    }

    fun checkSharedPreferencesKey(context: Context,key:String): String{
        val preferences :SharedPreferences= context.getSharedPreferences("firstAccessVariables",Context.MODE_PRIVATE)
        val sharedPreferencesKey = preferences.getString(key,"N")
        return sharedPreferencesKey
    }

    fun updateSharedPreference(context: Context,key:String, value:String){
        val preferences :SharedPreferences= context.getSharedPreferences("firstAccessVariables",Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(key,value)
        editor.commit()
    }
}