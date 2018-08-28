package org.frocu.news.mynot.Singletons

import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley

object imageLoaderVolley {
    lateinit var requestQueue: RequestQueue
    lateinit var imageLoader: ImageLoader

    fun initializeImageLoaderVolley(context: Context){
        requestQueue = Volley.newRequestQueue(context)
        imageLoader = ImageLoader(requestQueue, object : ImageLoader.ImageCache {
            private val cache = LruCache<String, Bitmap>(10)

            override fun putBitmap(url: String, bitmap: Bitmap) {
                cache.put(url, bitmap)
            }

            override fun getBitmap(url: String): Bitmap? {
                return cache.get(url)
            }
        })
    }
}