package org.frocu.news.mynot.Singletons

import org.frocu.news.mynot.Singletons.CCAAList.ccaaList
import org.frocu.news.mynot.Singletons.NewsItemList.news
import org.frocu.news.mynot.Singletons.NewspapersList.newspapers
import org.frocu.news.mynot.Singletons.SectionList.sections

object GlobalVariables {
    var positionNewspaperInCharge : Int = -1
    var urlNewsItemActual : String = ""
    var sectionActual : String = ""
    var colorActual : String = ""

    fun cleanAppArrays(){
        sections.clear()
        ccaaList.clear()
        newspapers.clear()
        news.clear()
    }
}