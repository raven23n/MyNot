package org.frocu.news.mynot.Databases

import org.frocu.news.mynot.POJO.NewsItem
import java.util.ArrayList

interface NewsSavedDatabase{
    fun saveNewsItem(newsItem: NewsItem)
    fun obtainNewsItemsSaved(): ArrayList<NewsItem>
    fun deleteNewsItem(newsItem: NewsItem)
}