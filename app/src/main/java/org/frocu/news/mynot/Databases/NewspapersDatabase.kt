package org.frocu.news.mynot.Databases

import org.frocu.news.mynot.POJO.Newspaper

interface NewspapersDatabase {
    public interface NewspapersListener {
        fun onRespuesta(newspapers: Newspaper?)
    }
    public fun readNewspaper(newspapersId: String, newspapersListener: NewspapersListener)
    public fun readNewspapers(): ArrayList<Newspaper>
}