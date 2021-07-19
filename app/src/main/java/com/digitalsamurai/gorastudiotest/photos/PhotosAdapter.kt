package com.digitalsamurai.gorastudiotest.photos

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.digitalsamurai.gorastudiotest.NetworkService
import com.digitalsamurai.gorastudiotest.Photo
import com.digitalsamurai.gorastudiotest.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PhotosAdapter(
    var data : ArrayList<Photo>
    ,var context : Context) : RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        with(LayoutInflater.from(context)){
            val view = this.inflate(R.layout.item_photo, parent, false)
            return PhotoViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.titleText.setText(data.get(position).title)
        holder.showLoading()
        holder.url = data.get(position).photoUrl
        holder.loadPhoto()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setPhotoInItem(id : Int , bitmap: Bitmap){

    }

    class PhotoViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var photo : ImageView
        var loadingBar : ProgressBar
        var titleText : TextView
        lateinit var url : String
        init {
            photo = itemView.findViewById(R.id.item_photo_image_photo)
            loadingBar = itemView.findViewById(R.id.item_photo_progressBar)
            titleText = itemView.findViewById(R.id.item_photo_textview_title)
        }
        fun setPhotoInItem(bitmap: Bitmap){
            photo.setImageBitmap(bitmap)
            showPhoto()
        }
        fun showLoading(){
            loadingBar.visibility = View.VISIBLE
            photo.visibility = View.INVISIBLE
        }
        fun showPhoto(){
            loadingBar.visibility = View.INVISIBLE
            photo.visibility = View.VISIBLE
        }

        fun loadPhoto() = GlobalScope.launch(Dispatchers.Main){

            with(NetworkService.getPhoto(url)){
                if (this !=null){
                    setPhotoInItem(this)

                } else{
                    setPhotoInItem(NetworkService.getPhotoByUrl(url).await())
                }
            }
        }

    }
}