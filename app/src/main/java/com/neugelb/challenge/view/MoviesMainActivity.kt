package com.neugelb.challenge.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.neugelb.challenge.R
import com.neugelb.challenge.view.extensions.Activities
import com.neugelb.challenge.view.extensions.intentTo

class MoviesMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies_main)

        //Open the Movies activity on app launch
        startActivity(intentTo(Activities.Movies))
        finish()
    }
}
