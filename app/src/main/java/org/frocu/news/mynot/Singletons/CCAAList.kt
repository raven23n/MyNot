package org.frocu.news.mynot.Singletons

import org.frocu.news.mynot.POJO.Section
import org.frocu.news.mynot.Singletons.SectionList.sections

object CCAAList {

    val staticCCAAName = arrayOf(
            "Andalucía",
            "Aragón",
            "Asturias",
            "Cantabria",
            "Castilla-La Mancha",
            "Castilla y León",
            "Cataluña",
            "Ceuta y Melilla",
            "C. Valenciana",
            "Extremadura",
            "Galicia",
            "Islas Baleares",
            "Islas Canarias",
            "La Rioja",
            "Madrid",
            "Murcia",
            "Navarra",
            "País Vasco"
    )

    val staticCCAAColor = "#A3FF8D"
    var ccaaList : ArrayList <Section> = ArrayList()

    fun getCCAAItem(pos: Int): Section? {
        return ccaaList[pos]

    }

    fun getCCAAKey(pos: Int): String {
        return ccaaList[pos].sectionName
    }

    fun getCCAACount(): Int {
        return ccaaList.size
    }

    fun orderByNumberOfAccessToCCAA(){

    }
}