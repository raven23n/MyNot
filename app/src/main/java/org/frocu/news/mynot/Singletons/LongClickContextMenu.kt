package org.frocu.news.mynot.Singletons

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import org.frocu.news.mynot.Singletons.NewsItemList.news
import org.frocu.news.mynot.Singletons.NewsSavedDatabaseObject.instanceDatabase

object LongClickContextMenu{

    fun createContextMenu(context: Context, view:View, position: Int): AlertDialog?
    {
        val menu = AlertDialog.Builder(context)
        val options = createArrayOptions(context)
        menu.setItems(options) { dialog, option ->
            when (option) {
                0 /*Guardar*/   -> saveNewsItem(context,position)
                1 /*Compartir*/ -> shareNewsItem(context,position)
            }
        }
        return menu.create()
    }

    fun createArrayOptions(context: Context): Array<CharSequence>{
        var options: Array<CharSequence> = arrayOf("")
        Log.d("ClassName ContextMenu","context.javaClass.canonicalName: -"+context.javaClass.canonicalName+"-")
        var nameOfClassThatCallsThisFun = giveNameOfClassThatCalls(context.javaClass.canonicalName)
        when(nameOfClassThatCallsThisFun){
            "NewsItemActivity"->{
                options = arrayOf("Guardar", "Compartir")
            }
        }
        return options
    }

    fun giveNameOfClassThatCalls(canonicalName:String):String{
        val parts = canonicalName.split(".")
        val nameOfClassThatCallsThisFun = parts[5]
        Log.d("ClassName ContextMenu","ClassName: -"+ nameOfClassThatCallsThisFun +"-")
        return nameOfClassThatCallsThisFun
    }

    fun shareNewsItem(context:Context,position: Int){
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, news[position].headlineOfANews)
        intent.putExtra(Intent.EXTRA_TEXT, news[position].urlOfANews)
        context.startActivity(Intent.createChooser(intent, "Compartir"))
    }

    fun saveNewsItem(context: Context, position: Int){
        instanceDatabase.saveNewsItem(news[position])
        Toast.makeText(
                context,
                "Noticia guardada correctamente",
                Toast.LENGTH_LONG
        ).show()
    }
}