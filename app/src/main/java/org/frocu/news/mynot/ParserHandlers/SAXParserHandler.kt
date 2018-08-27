package org.frocu.news.mynot.ParserHandlers

import org.frocu.news.mynot.POJO.NewsItem
import org.frocu.news.mynot.Singletons.NewsItemList.news
import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler
import java.util.ArrayList

class SAXParserHandler : DefaultHandler() {
    var newsInternalSAX: ArrayList<NewsItem> = ArrayList()
    var newsItemLoading: NewsItem = NewsItem()
    var text: StringBuilder = StringBuilder()
    val comma: Char = 0x0027.toChar()

    @Throws(SAXException::class)
    override fun startDocument() {
        newsInternalSAX = ArrayList<NewsItem>()
        text = StringBuilder()
    }

    @Throws(SAXException::class)
    override fun startElement(uri: String?, localName: String?, qName: String?, attributes: Attributes?) {
        if (localName == "item") {
            //newsItemLoading = NewsItem()
        }

        //ojo al attributes.getValue(String) para recuperar la url de las imagenes
        text.setLength(0)
    }

    @Throws(SAXException::class)
    override fun endElement(uri: String?, localName: String?, qName: String?) {
        if (newsItemLoading != null) {
            when(localName){
                "title" -> {
                    val headline = text.toString()
                    newsItemLoading.headlineOfANews = cleaningText(headline)
                }
                "pubDate" ->
                    newsItemLoading.dateOfANews = text.toString()
                "link" ->
                    newsItemLoading.urlOfANews = text.toString()
                "item" -> {
                    news.add(newsItemLoading)
                    newsItemLoading = NewsItem()
                }
            }
        }
    }

    @Throws(SAXException::class)
    override fun characters(ch: CharArray?, start: Int, length: Int) {
        text.append(ch, start, length)
    }

    fun cleaningText(miTexto: String): String {
        var salida = String()
        var guardar = true
        for (i in 0 until miTexto.length) {
            if (miTexto[i] == '<') {
                guardar = false
            } else {
                if (guardar) {
                    if (miTexto[i] != comma) {
                        salida += miTexto[i]
                    }
                } else {
                    if (miTexto[i] == '>') {
                        guardar = true
                    }
                }
            }
        }
        return salida
    }
}
