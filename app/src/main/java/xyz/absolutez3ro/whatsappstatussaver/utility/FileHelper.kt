package xyz.absolutez3ro.whatsappstatussaver.utility

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile

object FileHelper {
//    private const val TAG = "FileHelper"

    private fun getSourceDocument(context: Context): DocumentFile? {
        val root = DocumentFile.fromTreeUri(
            context,
            Uri.parse(PreferenceHelper.getString(context, Constants.EXTERNAL_URI_KEY, ""))
        )
        return root?.findFile("WhatsApp")?.findFile("Media")
            ?.findFile(".Statuses")
    }

    private fun getDestinationDir(context: Context): DocumentFile {
        val root = DocumentFile.fromTreeUri(
            context,
            Uri.parse(PreferenceHelper.getString(context, Constants.EXTERNAL_URI_KEY, ""))
        )
        var pictures = root?.findFile("Pictures")
        if (pictures == null) pictures = root?.createDirectory("Pictures")
        var wa = pictures?.findFile("WhatsApp Status")
        if (wa == null) wa = pictures?.createDirectory("WhatsApp Status")
        return wa!!
    }


    fun listStatuses(context: Context): ArrayList<DocumentFile> {
        val photoList: ArrayList<DocumentFile> = arrayListOf()
        val statusDir = getSourceDocument(context)
        statusDir?.listFiles()?.forEach {
            //            Log.d(TAG, it.uri.toString())
            photoList.add(it)
        }
        return photoList
    }

    fun saveItem(context: Context, sourceFile: DocumentFile) {
        val destinationDir = getDestinationDir(context)
        if (destinationDir.findFile(sourceFile.name!!) != null) {
            Toast.makeText(context, "Already Saved", Toast.LENGTH_LONG).show()
            return
        }
        val targetFile = destinationDir.createFile(sourceFile.type!!, sourceFile.name!!)
        val inputStream = context.contentResolver.openInputStream(sourceFile.uri)
        val outputStream = context.contentResolver.openOutputStream(targetFile!!.uri)
        if (inputStream != null && outputStream != null) {
            inputStream.copyTo(outputStream, DEFAULT_BUFFER_SIZE)
            inputStream.close()
            outputStream.flush()
            outputStream.close()
            Toast.makeText(context, "Saved at /Pictures/WhatsApp Status/", Toast.LENGTH_LONG).show()
        } else Toast.makeText(context, "Failed to save", Toast.LENGTH_LONG).show()

    }


}