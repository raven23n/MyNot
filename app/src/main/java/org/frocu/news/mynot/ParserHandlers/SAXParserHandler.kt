package org.frocu.news.mynot.ParserHandlers

import android.util.Log
import org.frocu.news.mynot.POJO.NewsItem
import org.frocu.news.mynot.Singletons.GlobalVariables.positionNewspaperInCharge
import org.frocu.news.mynot.Singletons.NewsItemList.news
import org.frocu.news.mynot.Singletons.NewspapersList.newspapers
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
    var isItemDescription: Boolean = false

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
                isItemDescription = true
            }
            "media:thumbnail"-> {
                if(isFirstImage) {
                    var urlImage = attributes?.getValue("url").toString()
                    newsItemLoading.imageOfANews = urlImage
                    Log.d("SAX Parser: ", "Imagen url: -" + urlImage + "-")
                    isFirstImage = false
                }
            }
            "enclosure"-> {
                if(isFirstImage) {
                    var urlImage = attributes?.getValue("url").toString()
                    newsItemLoading.imageOfANews = urlImage
                    Log.d("SAX Parser: ", "Imagen url: -" + urlImage + "-")
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
                "description" -> {
                    if(isItemDescription) {
                        when (newspapers[positionNewspaperInCharge].nameNewspaper) {
                            "20 Minutos", "ABC" -> {
                                val urlImage = searchImageInDescription(text.toString())
                                newsItemLoading.imageOfANews = urlImage
                                isFirstImage = false
                            }
                        }
                    }
                }
                "link" -> {
                    newsItemLoading.urlOfANews = text.toString()
                    Log.d("SAX Parser: ", "URL: -" + newsItemLoading.urlOfANews + "-")
                }

                "pubDate" -> {
                    newsItemLoading.dateOfANews = cleanPubDate(text.toString())
                    Log.d("SAX Parser: ", "Fecha: -" + newsItemLoading.dateOfANews + "-")
                }
                "dc:date" -> {
                    val dcDateString = text.toString()
                    newsItemLoading.dateOfANews = cleanDcDate(dcDateString)
                    Log.d("SAX Parser: ", "Fecha: -" + newsItemLoading.dateOfANews + "-")
                }
                "item" -> {
                    news.add(newsItemLoading)
                    newsItemLoading = NewsItem()
                    isFirstImage = true
                    Log.d("SAX Parser: ", "Fin item item")
                }
            }
        }
    }

    @Throws(SAXException::class)
    override fun characters(ch: CharArray?, start: Int, length: Int) {
        text.append(ch, start, length)
    }

    fun cleaningText(textToClean: String): String {
        var textCleaned = String()
        var saveChar = true
        for (i in 0 until textToClean.length) {
            if (textToClean[i] == '<') {
                saveChar = false
            } else {
                if (saveChar) {
                    if (textToClean[i] != comma) {
                        textCleaned += textToClean[i]
                    }
                } else {
                    if (textToClean[i] == '>') {
                        saveChar = true
                    }
                }
            }
        }
        return textCleaned
    }

    fun cleanPubDate(pubDateString: String): String {
        var pubDateCleaned = pubDateString
        var indexOfPlus = pubDateString.indexOf('+')
        if (indexOfPlus > 0)
            pubDateCleaned = pubDateString.substring(0,indexOfPlus)
        return pubDateCleaned
    }

    fun cleanDcDate(dcDateString: String): String {
        var dcDateCleaned = String()
        var saveChar = true
        for (i in 0 until dcDateString.length) {
            if (dcDateString[i] == 'T') {
                dcDateCleaned += ' '
            } else {
                if (dcDateString[i] == '+') {
                    saveChar = false
                } else {
                    if (saveChar) {
                        dcDateCleaned += dcDateString[i]
                    }
                }
            }
        }
        return dcDateCleaned
    }


    fun searchImageInDescription(description: String): String {
        val indexOfImg = description.indexOf("img")
        val indexOfSrc = description.indexOf("src",indexOfImg)
        val indexOfInitialDobleComma = description.indexOf('"',indexOfSrc)
        val indexOfFinalDobleComma = description.indexOf('"',indexOfInitialDobleComma+1)
        val urlImage = description.substring(indexOfInitialDobleComma+1, indexOfFinalDobleComma)
/*        Log.d("SAX Parser: ", "indexOfImg -" + indexOfImg + "-")
        Log.d("SAX Parser: ", "indexOfSrc -" + indexOfSrc + "-")
        Log.d("SAX Parser: ", "indexOfInitialDobleComma -" + indexOfInitialDobleComma + "-")
        Log.d("SAX Parser: ", "indexOfFinalDobleComma -" + indexOfFinalDobleComma + "-")
        Log.d("SAX Parser: ", "urlImage -" + urlImage + "-")*/
        return urlImage
    }
}
