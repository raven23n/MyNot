package org.frocu.news.mynot.Databases

interface SectionDatabase {

    public interface SectionsListener {
        fun onRespuesta(endOfQuery: Boolean)
    }
    public fun readSections(sectionsListener:SectionsListener);
}