package com.digitalsamurai.gorastudiotest.photos

import android.content.Context
import com.digitalsamurai.gorastudiotest.Photo
import androidx.recyclerview.widget.RecyclerView
import com.digitalsamurai.gorastudiotest.NetworkAccess
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

    //загружает фото и ставит в ресайклер
    //проверку интернета делаем на это стадии, чтобы класс NetworkService не использовал механики Андроид

    fun setPhotosInRecycler() = GlobalScope.launch(Dispatchers.Main) {
        if (NetworkAccess.isNetworkConnected(context)) {

            view.showLoading()
            var albums = NetworkService.getAlbumsByUserId(userId).await()
            var photosList = ArrayList<Photo>()
            albums.forEach {
                photosList.addAll(NetworkService.getPhotosByAlbumId(it.id).await() as List<Photo>)

            }
            data.addAll(photosList)
            adapter.notifyDataSetChanged()
            view.showDataLyout()
        } else{
            view.showError()
        }
    }

}