package org.frocu.news.mynot.Databases

interface SectionDatabase {

    interface SectionsListener {
        fun onRespuesta(endOfQuery: Boolean)
    }
    fun readSections(sectionsListener:SectionsListener);
    fun readCCAA(sectionsListener:SectionsListener);
}