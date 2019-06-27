package xyz.absolutez3ro.whatsappstatussaver.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import xyz.absolutez3ro.whatsappstatussaver.R
import xyz.absolutez3ro.whatsappstatussaver.utility.FileHelper

class PhotoAdapter(private var photoList: ArrayList<DocumentFile>, private val context: Context) :
    RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = photoList[position]
        Glide.with(context).load(photo.uri).into(holder.thumbnail)
        holder.saveButton.setOnClickListener { FileHelper.saveItem(context, photo) }
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    fun setList(photoList: ArrayList<DocumentFile>) {
        this.photoList = photoList
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thumbnail: ImageView = view.findViewById(R.id.imageView_item)
        val saveButton: MaterialButton = view.findViewById(R.id.button_save)
    }
}