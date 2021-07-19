package com.digitalsamurai.gorastudiotest.main

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.digitalsamurai.gorastudiotest.NetworkAccess
import com.digitalsamurai.gorastudiotest.NetworkService
import com.digitalsamurai.gorastudiotest.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PresenterMainActivity(private var view : InterfaceMainActivity,
                            recycler : RecyclerView,
                            val context : Context) {

    private var recyclerData = ArrayList<User>()
    private var adapter : UsersAdapter

    init {
        adapter = UsersAdapter(recyclerData,context)
        recycler.adapter = adapter
    }
    fun setUsersInRecycler() = GlobalScope.launch(Dispatchers.Main) {
        if (NetworkAccess.isNetworkConnected(context)) {

            view.showLoading()

            var users = NetworkService.getUsers().await()
            recyclerData.addAll(users)

            view.showDataLayout()
        } else{
            view.showError()
        }

    }

}