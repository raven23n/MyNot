package org.frocu.news.mynot.Databases

import android.util.Log
import org.frocu.news.mynot.POJO.Section
import org.frocu.news.mynot.Singletons.CCAAList.ccaaList
import org.frocu.news.mynot.Singletons.CCAAList.staticCCAAColor
import org.frocu.news.mynot.Singletons.CCAAList.staticCCAAName
import org.frocu.news.mynot.Singletons.SectionList.sections
import org.frocu.news.mynot.Singletons.SectionList.staticSectionColor
import org.frocu.news.mynot.Singletons.SectionList.staticSectionName

class SectionDatabaseArray :SectionDatabase{
    override fun searchSection(sectionType: String) {
        when(sectionType) {
            "S" -> {
                sections.clear()
                for(i in 0..7){
                    var sectionReceived = Section(
                            sectionName = staticSectionName[i],
                            sectionColour = staticSectionColor[i]
                    )
                    sections.add(sectionReceived)
                }
            }
            "C" -> {
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
    }

    override fun saveSections(sectionType: String) {

    }

    override fun updateCountSections(section: Section, sectionType: String, position: Int) {
        when(sectionType) {
            "S" -> {
                sections[position].numberOfAccessToSection ++
                Log.d("updateSectionsArray", "contador sections: -"+sections[position].numberOfAccessToSection + "-")
            }
            "C" -> {
                ccaaList[position].numberOfAccessToSection ++
                Log.d("updateSectionsArray", "contador ccaa: -"+ ccaaList[position].numberOfAccessToSection + "-")
            }
        }
    }

}