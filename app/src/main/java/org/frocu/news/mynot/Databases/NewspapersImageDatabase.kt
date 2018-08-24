package org.frocu.news.mynot.Databases

import android.graphics.Bitmap

interface NewspapersImageDatabase {
    public interface NewspaperImageListener {
        fun onRespuesta(image: Bitmap?)
    }
    public fun takeImageNewspaper(newspaperImageListener: NewspaperImageListener)
}