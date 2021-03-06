package org.frocu.news.mynot.Databases

import android.content.Context
import org.frocu.news.mynot.POJO.Newspaper

interface NewspapersDatabase {
    public interface NewspapersListener {
        fun onRespuesta(endOfQuery: Boolean)
    }
    public fun readNewspapers(newspapersListener: NewspapersDatabase.NewspapersListener, context: Context)
    public fun readCCAANewspapers(country:String,
                                  autonomous_communities: String,
                                  newspapersListener: NewspapersDatabase.NewspapersListener, context: Context)
}
//public fun readNewspaper(newspapersId: String, newspapersListener: NewspapersDatabase.NewspapersListener)