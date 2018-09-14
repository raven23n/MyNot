package org.frocu.news.mynot.Singletons

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.widget.Toast
import org.frocu.news.mynot.Activities.NewsItemActivity
import org.frocu.news.mynot.Singletons.GlobalVariablesAndFuns.positionNewspaperInCharge
import org.frocu.news.mynot.Singletons.NewsItemList.news
import org.frocu.news.mynot.Singletons.NewsSavedDatabaseObject.instanceDatabase

object LongClickContextMenu{

    fun createContextMenu(context: Context, position: Int): AlertDialog?
    {
        val menu = AlertDialog.Builder(context)
        val options = createArrayOptions()
        menu.setItems(options) { dialog, option ->
            when (option) {
                0 /*Guardar o Borrar*/   -> {
                    when(positionNewspaperInCharge){
                        -1->    deleteNewsItem(context,position)
                        else -> saveNewsItem(context,position)
                    }
                }
                1 /*Compartir*/ -> shareNewsItem(context,position)
            }
        }
        return menu.create()
    }

    fun createArrayOptions(): Array<CharSequence>{
        when(positionNewspaperInCharge){
            -1->    return arrayOf("Borrar", "Compartir")
            else -> return arrayOf("Guardar", "Compartir")
        }
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

    fun deleteNewsItem(context: Context, position: Int){
        instanceDatabase.deleteNewsItem(news[position])
        news.removeAt(position)
        Toast.makeText(
                context,
                "Noticia borrada correctamente",
                Toast.LENGTH_LONG
        ).show()

        if (context is NewsItemActivity) {
            context.newsItemAdapterNotifyDataSetChanged()
        }
    }
}
/*
    fun giveNameOfClassThatCalls(canonicalName:String):String{
        val parts = canonicalName.split(".")
        val nameOfClassThatCallsThisFun = parts[5]
        Log.d("ClassName ContextMenu","ClassName: -"+ nameOfClassThatCallsThisFun +"-")
        return nameOfClassThatCallsThisFun
    }*/
/*
fun createArrayOptions(): Array<CharSequence>{
    var options: Array<CharSequence> = arrayOf("")
    Log.d("ClassName ContextMenu","context.javaClass.canonicalName: -"+context.javaClass.canonicalName+"-")
    var nameOfClassThatCallsThisFun = giveNameOfClassThatCalls(context.javaClass.canonicalName)
    when(nameOfClassThatCallsThisFun){
        "NewsItemActivity"->{
            options = arrayOf("Guardar", "Compartir")
        }
    }
    return options
}*/
