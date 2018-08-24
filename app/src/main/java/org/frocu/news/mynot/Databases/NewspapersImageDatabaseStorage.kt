package org.frocu.news.mynot.Databases

import android.graphics.Bitmap
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import android.graphics.BitmapFactory
import com.google.android.gms.tasks.OnSuccessListener
import org.frocu.news.mynot.R


class NewspapersImageDatabaseStorage(val imageRef:String)
    : NewspapersImageDatabase{

    private var newspaperImageReference : StorageReference

    init {
        var imageDB: FirebaseStorage = FirebaseStorage.getInstance()
        newspaperImageReference = imageDB.getReferenceFromUrl("gs://mynot-2b4a3.appspot.com")
    }

    override fun takeImageNewspaper(newspaperImageListener : NewspapersImageDatabase.NewspaperImageListener) {
        val bitmap = arrayOfNulls<Bitmap>(1)
        val storage = FirebaseStorage.getInstance().getReference(imageRef+".png")
        storage.getBytes((24 * 1024).toLong())
                .addOnSuccessListener {
                    object: OnSuccessListener<ByteArray> {
                        override fun onSuccess(imageBytes: ByteArray) {
                            bitmap[0] = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size);
                            newspaperImageListener.onRespuesta(bitmap[0])
                        }
                    }
                }
    }
}
