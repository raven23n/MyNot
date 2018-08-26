package org.frocu.news.mynot.ParserHandlers

import org.frocu.news.mynot.POJO.NewsItem
import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler
import java.util.ArrayList

class SAXParserHandler : DefaultHandler() {
    var newsInternalSAX: ArrayList<NewsItem> = ArrayList()
    var noticiaActual: NewsItem = NewsItem()
    var texto: StringBuilder = StringBuilder()
    val comilla: Char = 0x0027.toChar()

    @Throws(SAXException::class)
    override fun startDocument() {
        newsInternalSAX = ArrayList<NewsItem>()
        texto = StringBuilder()
    }

    @Throws(SAXException::class)
    override fun startElement(uri: String?, localName: String?, qName: String?, attributes: Attributes?) {
        if (localName == "item") {
            noticiaActual = NewsItem()
        }

        //ojo al attributes.getValue(String) para recuperar la url de las imagenes
        texto.setLength(0)
    }

    @Throws(SAXException::class)
    override fun endElement(uri: String?, localName: String?, qName: String?) {
        if (noticiaActual != null) {
            if (localName == "title") {
                val miTitular = texto.toString()
        //        noticiaActual!!.setTitular(limpiarTexto(miTitular))
            } else if (localName == "description") {
                val miTexto = texto.toString()
      //          noticiaActual!!.setCuerpo(limpiarTexto(miTexto))
            } else if (localName == "pubDate") {
    //            noticiaActual!!.setFecha(texto.toString())
            } else if (localName == "link") {
  //              noticiaActual!!.setUrl(texto.toString())
            } else if (localName == "item") {
//                noticias.add(noticiaActual)
//                noticiaActual = null
            }
            when(localName){
                "title" -> ""
                "description" -> ""
                "pubDate" -> ""
                "link" -> ""
                "item" -> ""
            }
        }
    }

    @Throws(SAXException::class)
    override fun characters(ch: CharArray?, start: Int, length: Int) {
        texto.append(ch, start, length)
    }

    fun limpiarTexto(miTexto: String): String {
        var salida = String()
        var guardar = true
        for (i in 0 until miTexto.length) {
            if (miTexto[i] == '<') {
                guardar = false
            } else {
                if (guardar) {
                    if (miTexto[i] != comilla) {
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
