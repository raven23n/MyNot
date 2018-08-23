package org.frocu.news.mynot.Singletons

import org.frocu.news.mynot.POJO.Newspaper

object NewspapersList {
    var newspapers : ArrayList <Newspaper> = ArrayList()
/*    var loaded: Boolean = false

    fun changeLoaded (state : Boolean){
        loaded = state
    }

    fun getLoaded (state : Boolean): Boolean {
        return loaded
    }*/

    fun getItem(pos: Int): Newspaper? {
        return newspapers[pos]
    }

    fun getKey(pos: Int): String {
        return newspapers[pos].nameNewspaper
    }

    fun getItemCount(): Int {
        return newspapers.size
    }
}