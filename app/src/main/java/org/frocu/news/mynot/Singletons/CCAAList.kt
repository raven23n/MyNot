package org.frocu.news.mynot.Singletons

import org.frocu.news.mynot.POJO.Section
import org.frocu.news.mynot.Singletons.SectionList.sections

object CCAAList {
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