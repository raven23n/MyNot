package org.frocu.news.mynot.ParserHandlers

import android.util.Log
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
    var isFirstImage: Boolean = true

    @Throws(SAXException::class)
    override fun startDocument() {
        newsInternalSAX = ArrayList<NewsItem>()
        text = StringBuilder()
    }

    @Throws(SAXException::class)
    override fun startElement(uri: String?, localName: String?, qName: String?, attributes: Attributes?) {
        when(qName){
            "item" -> {
                Log.d("SAX Parser: ", "Inicio Item")
            }
            "img"-> {
                Log.d("SAX Parser: ", "Imagen: -" + text.toString() + "-")
                Log.d("SAX Parser: ", "LocalName: -" + localName + "-")
                Log.d("SAX Parser: ", "Uri: -" + uri + "-")
                Log.d("SAX Parser: ", "QName: -" + qName + "-")
            }
            "media:thumbnail"-> {
                if(isFirstImage) {
                    var urlImagen = attributes?.getValue("url").toString()
                    newsItemLoading.imageOfANews = urlImagen
                    Log.d("SAX Parser: ", "Imagen url: -" + urlImagen + "-")
                    Log.d("SAX Parser: ", "LocalName: -" + localName + "-")
                    Log.d("SAX Parser: ", "Uri: -" + uri + "-")
                    Log.d("SAX Parser: ", "QName: -" + qName + "-")
                    isFirstImage = false
                }
            }
            "enclosure"-> {
                if(isFirstImage) {
                    var urlImagen = attributes?.getValue("url").toString()
                    newsItemLoading.imageOfANews = urlImagen
                    Log.d("SAX Parser: ", "Imagen url: -" + urlImagen + "-")
                    Log.d("SAX Parser: ", "LocalName: -" + localName + "-")
                    Log.d("SAX Parser: ", "Uri: -" + uri + "-")
                    Log.d("SAX Parser: ", "QName: -" + qName + "-")
                    isFirstImage = false
                }
            }
        }
        text.setLength(0)
    }

    @Throws(SAXException::class)
    override fun endElement(uri: String?, localName: String?, qName: String?) {
        if (newsItemLoading != null) {
            when(qName){
                "title" -> {
                    val headline = text.toString()
                    newsItemLoading.headlineOfANews = cleaningText(headline)
                    Log.d("SAX Parser: ", "Titulo: -" + newsItemLoading.headlineOfANews + "-")
                }
                "pubDate" -> {
                    newsItemLoading.dateOfANews = text.toString()
                    Log.d("SAX Parser: ", "Fecha: -" + newsItemLoading.dateOfANews + "-")
                }
                "link" -> {
                    newsItemLoading.urlOfANews = text.toString()
                    Log.d("SAX Parser: ", "URL: -" + newsItemLoading.urlOfANews + "-")
                }
                "item" -> {
                    news.add(newsItemLoading)
                    newsItemLoading = NewsItem()
                    isFirstImage = true
                    Log.d("SAX Parser: ", "Fin item")
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
