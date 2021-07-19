package com.digitalsamurai.gorastudiotest.photos

import android.content.Context
import com.digitalsamurai.gorastudiotest.Photo
import androidx.recyclerview.widget.RecyclerView
import com.digitalsamurai.gorastudiotest.NetworkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PresenterPhotoActivity(private val view : InterfacePhotoActivity,
                             private val context  : Context,
                             private var recycler : RecyclerView,
                             private val userId : Int) {
    private var adapter : PhotosAdapter
    private var data : ArrayList<Photo>
    init {
        data = ArrayList()
        adapter = PhotosAdapter(data, context)
        recycler.adapter = adapter
    }

    fun setPhotosInRecycler() = GlobalScope.launch(Dispatchers.Main){
        view.showLoading()
        var albums  = NetworkService.getAlbumsByUserId(userId).await()
        var photosList = ArrayList<Photo>()
        albums.forEach {
            photosList.addAll(NetworkService.getPhotosByAlbumId(it.id).await() as List<Photo>)

        }
        data.addAll(photosList)
        adapter.notifyDataSetChanged()
        view.showDataLyout()
        for(i : Int in 0..data.size-1){
            val bitmap = NetworkService.getPhotoByUrl(data.get(i).photoUrl).await()
            //adapter.setPhotoInItem(i,bitmap)
            //(recycler.findViewHolderForLayoutPosition(i) as PhotosAdapter.PhotoViewHolder).setPhotoInItem(bitmap)
        }
    }

}