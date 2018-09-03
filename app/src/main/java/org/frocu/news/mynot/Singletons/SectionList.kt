package org.frocu.news.mynot.Singletons

import android.graphics.Color
import org.frocu.news.mynot.POJO.Section

object SectionList {
    val staticSectionName = arrayOf(
        "C. Autónomas",
        "Ciencia",
        "Deportes",
        "Economía",
        "Internacional",
        "Nacional",
        "Tecnología",
        "Últimas Noticias"
    )

    val staticSectionColor = arrayOf(
        "#A3FF8D",
        "#E8CDB0",
        "#FFEC97",
        "#FF9CD8",
        "#858FE8",
        "#E87378",
        "#E0E0E0",
        "#CE93D8"
    )

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