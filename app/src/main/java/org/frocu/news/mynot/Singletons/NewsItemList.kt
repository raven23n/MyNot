package org.frocu.news.mynot.Singletons

import org.frocu.news.mynot.POJO.NewsItem

object NewsItemList {
    var news : ArrayList <NewsItem> = ArrayList()

    fun getItem(pos: Int): NewsItem? {
        return news[pos]
    }

    fun getItemCount(): Int {
        return news.size
    }

    fun getKey(pos: Int): String {
        return news[pos].headlineOfANews
    }
}