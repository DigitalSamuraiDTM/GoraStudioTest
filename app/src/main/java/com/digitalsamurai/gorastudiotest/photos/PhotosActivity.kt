package com.digitalsamurai.gorastudiotest.photos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.digitalsamurai.gorastudiotest.R

class PhotosActivity : AppCompatActivity(), InterfacePhotoActivity {

    private lateinit var presenter : PresenterPhotoActivity
    private lateinit var recyclerPhotos : RecyclerView
    private lateinit var progressLoading : ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos)

        setTitle("Photos")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        progressLoading = findViewById(R.id.act_photos_progressBar_loading)
        recyclerPhotos = findViewById(R.id.act_photos_recycler_photos)
        presenter = PresenterPhotoActivity(this, applicationContext, recyclerPhotos, intent?.getIntExtra("userId", 0)!!)
        presenter.setPhotosInRecycler()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun showLoading() {
        recyclerPhotos.visibility = View.GONE
        progressLoading.visibility = View.VISIBLE
    }

    override fun showDataLyout() {
        recyclerPhotos.visibility = View.VISIBLE
        progressLoading.visibility = View.GONE

    }

    override fun showError() {
        recyclerPhotos.visibility = View.VISIBLE
        progressLoading.visibility = View.GONE
        Toast.makeText(this, "something wrong", Toast.LENGTH_SHORT).show()

    }
}