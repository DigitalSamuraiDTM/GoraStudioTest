package com.digitalsamurai.gorastudiotest.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.digitalsamurai.gorastudiotest.NetworkService
import com.digitalsamurai.gorastudiotest.R

class MainActivity : AppCompatActivity(), InterfaceMainActivity {
    private lateinit var presenter : PresenterMainActivity
    private lateinit var recyclerUsers : RecyclerView
    private lateinit var progressLoading : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerUsers = findViewById(R.id.act_main_recycler_users)
        progressLoading = findViewById(R.id.act_main_progressBar_loading)
        presenter = PresenterMainActivity(this, recyclerUsers, this)

        setTitle("Users")
        presenter.setUsersInRecycler()
    }

    override fun showLoading() {
        recyclerUsers.visibility = View.GONE
        progressLoading.visibility= View.VISIBLE
    }

    override fun showDataLayout() {
        recyclerUsers.visibility = View.VISIBLE
        progressLoading.visibility= View.GONE

    }

    override fun showError() {
        recyclerUsers.visibility = View.VISIBLE
        progressLoading.visibility= View.GONE
        Toast.makeText(this, "something wrong", Toast.LENGTH_SHORT).show()

    }
}