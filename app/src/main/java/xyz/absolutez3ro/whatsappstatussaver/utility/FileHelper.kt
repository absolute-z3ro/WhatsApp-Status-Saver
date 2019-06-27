package xyz.absolutez3ro.whatsappstatussaver.utility

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import xyz.absolutez3ro.whatsappstatussaver.model.Photo

object FileHelper {
    private const val TAG = "FileHelper"

    private fun getSourceDocument(context: Context): DocumentFile? {
        val root = DocumentFile.fromTreeUri(
            context,
            Uri.parse(PreferenceHelper.getString(context, Constants.EXTERNAL_URI_KEY, ""))
        )
        return root?.findFile("WhatsApp")?.findFile("Media")
            ?.findFile(".Statuses")
    }

    fun getDestinationUri(context: Context): Uri {
        val root = DocumentFile.fromTreeUri(
            context,
            Uri.parse(PreferenceHelper.getString(context, Constants.EXTERNAL_URI_KEY, ""))
        )
        var pictures = root?.findFile("Pictures")
        if (pictures == null) pictures = root?.createDirectory("Pictures")
        var wa = pictures?.findFile("WhatsApp Status")
        if (wa == null) wa = pictures?.createDirectory("WhatsApp Status")
        return wa!!.uri
    }


    fun listStatuses(context: Context): ArrayList<Photo> {
        val photoList: ArrayList<Photo> = ArrayList()
        val statusDir = getSourceDocument(context)
        statusDir?.listFiles()?.forEach {
            //            Log.d(TAG, it.uri.toString())
            photoList.add(Photo(it.uri, it.name!!, it.type!!))
        }
        return photoList
    }

    fun saveItem(context: Context, sourceUri: Uri, sourceName: String, sourceMime: String) {

    }


}