package org.frocu.news.mynot.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebChromeClient
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.news_web_view.*
import org.frocu.news.mynot.R
import org.frocu.news.mynot.Singletons.GlobalVariables
import org.frocu.news.mynot.Singletons.GlobalVariables.urlNewsItemActual


class NewsWebViewActivity : AppCompatActivity() {

    var urlNews =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.news_web_view)
    }

    override fun onResume() {
        super.onResume()
        webview_news_details.settings.javaScriptEnabled
        //webview_news_details.settings.builtInZoomControls
        webview_news_details.webViewClient = object: WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }
        }
        webview_news_details.webChromeClient = object: WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressbar_web_view.progress = 0
                progressbar_web_view.visibility = View.VISIBLE
                this@NewsWebViewActivity.setProgress(newProgress  * 1000)
                progressbar_web_view.incrementProgressBy(newProgress)
                if (newProgress == 100) {
                    progressbar_web_view.visibility = View.GONE
                }
            }
        }
        /*val extras = intent.extras
        urlNews = extras!!.getString("urlNews")
        webview_news_details.loadUrl(urlNews)*/
        webview_news_details.loadUrl(urlNewsItemActual)
    }
}
