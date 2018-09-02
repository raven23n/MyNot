package org.frocu.news.mynot.Singletons

import org.frocu.news.mynot.POJO.Section

object SectionList {
    var sections : ArrayList <Section> = ArrayList()

    fun getSectionItem(pos: Int): Section? {
        return sections[pos]

    }

    fun getSectionKey(pos: Int): String {
        return sections[pos].sectionName
    }

    fun getSectionCount(): Int {
        return sections.size
    }

    fun orderByNumberOfAccessToSection(){

    }
}