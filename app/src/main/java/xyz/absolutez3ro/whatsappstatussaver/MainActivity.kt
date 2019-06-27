package xyz.absolutez3ro.whatsappstatussaver

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import xyz.absolutez3ro.whatsappstatussaver.adapter.PhotoAdapter
import xyz.absolutez3ro.whatsappstatussaver.model.Photo
import xyz.absolutez3ro.whatsappstatussaver.utility.Constants
import xyz.absolutez3ro.whatsappstatussaver.utility.FileHelper
import xyz.absolutez3ro.whatsappstatussaver.utility.PreferenceHelper

class MainActivity : AppCompatActivity() {

    private var photoList: ArrayList<Photo> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (PreferenceHelper.getBoolean(this, Constants.FIRST_RUN, true))
            initiateStorageAccessFramework()
        else setRecyclerView()
    }

    private fun setRecyclerView() {
        val adapter = PhotoAdapter(photoList, this)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(this, 3)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
        recyclerView.setItemViewCacheSize(3)
        loadPhotos(adapter)
    }

    private fun initiateStorageAccessFramework() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        intent.addFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        )
        startActivityForResult(intent, Constants.EXTERNAL_STORAGE_ACTIVITY_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.EXTERNAL_STORAGE_ACTIVITY_CODE && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            if (uri != null) {
                PreferenceHelper.putString(this, Constants.EXTERNAL_URI_KEY, uri.toString())
                val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                contentResolver.takePersistableUriPermission(uri, takeFlags)
                PreferenceHelper.putBoolean(this, Constants.FIRST_RUN, false)
                setRecyclerView()
            } else PreferenceHelper.putString(this, Constants.EXTERNAL_URI_KEY, "")
        }
    }

    private fun showPermissionDialog() {
        val alertDialog = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle("Storage Permission required!")
                setMessage("Storage permissions are required for saving status.")
                setPositiveButton(android.R.string.ok) { _, _ -> finish() }
                setOnDismissListener { finish() }
            }
            builder.create()
        }
        alertDialog.show()
    }

    private fun loadPhotos(adapter: PhotoAdapter) {
        doAsync {
            photoList = FileHelper.listStatuses(this@MainActivity)
            uiThread {
                adapter.setList(photoList)
                adapter.notifyDataSetChanged()
            }
        }
    }
}
