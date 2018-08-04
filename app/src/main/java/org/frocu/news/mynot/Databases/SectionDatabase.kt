package org.frocu.news.mynot.Databases

import org.frocu.news.mynot.NewsSection

interface SectionDatabase {
    public fun readSections():ArrayList<NewsSection>;
}