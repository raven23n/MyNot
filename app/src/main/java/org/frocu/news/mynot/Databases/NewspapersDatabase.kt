package org.frocu.news.mynot.Databases

import org.frocu.news.mynot.POJO.Newspaper

interface NewspapersDatabase {
    public interface NewspapersListener {
        fun onRespuesta(newspapersList: ArrayList<Newspaper>)
    }
    public fun readNewspapers(newspapersListener: NewspapersDatabase.NewspapersListener)
    public fun readCCAANewspapers(country:String,
                                  autonomous_communities: String,
                                  newspapersListener: NewspapersDatabase.NewspapersListener)
}
//public fun readNewspaper(newspapersId: String, newspapersListener: NewspapersDatabase.NewspapersListener)