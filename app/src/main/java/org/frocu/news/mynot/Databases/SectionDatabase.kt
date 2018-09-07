package org.frocu.news.mynot.Databases

import org.frocu.news.mynot.POJO.Section

interface SectionDatabase {
    fun searchSection(sectionType : String)
    fun saveSections(sectionType : String)
    fun updateCountSections(section : Section, sectionType: String, position: Int)
}