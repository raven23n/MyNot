package org.frocu.news.mynot.Databases

import org.frocu.news.mynot.POJO.Section
import org.frocu.news.mynot.Singletons.CCAAList.ccaaList
import org.frocu.news.mynot.Singletons.CCAAList.staticCCAAColor
import org.frocu.news.mynot.Singletons.CCAAList.staticCCAAName
import org.frocu.news.mynot.Singletons.SectionList.sections
import org.frocu.news.mynot.Singletons.SectionList.staticSectionColor
import org.frocu.news.mynot.Singletons.SectionList.staticSectionName


class SectionDatabaseFirestore :SectionDatabase{

    override fun readSections() {
        sections.clear()
        for(i in 0..7){
            var sectionReceived = Section(
                    sectionName = staticSectionName[i],
                    sectionColour = staticSectionColor[i]
            )
            sections.add(sectionReceived)
        }
    }

    override fun readCCAA() {
        ccaaList.clear()
        for (i in 0..17) {
            var sectionReceived = Section(
                    sectionName = staticCCAAName[i],
                    sectionColour = staticCCAAColor
            )
            ccaaList.add(sectionReceived)
        }
    }
}