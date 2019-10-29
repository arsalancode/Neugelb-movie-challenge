package com.neugelb.challenge.movies.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.neugelb.challenge.movies.R
import com.neugelb.challenge.movies.dh.MovieDH
import com.neugelb.challenge.movies.model.MovieSearchModel

//Koin setup
private val loadMovieDependencies by lazy {  MovieDH.init() }

private fun injectMovieDependencies() =
    loadMovieDependencies

class MovieActivity : AppCompatActivity(),
    MovieSearchFragment.MovieNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        injectMovieDependencies()

        displayFragment(
            MovieSearchFragment.newInstance(),
            MovieSearchFragment.TAG
        )
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1)
            super.onBackPressed()
        else finish()
    }

    //Movie navigator interactions
    override fun showMovieDetails(movie: MovieSearchModel) {
        displayFragment(
            MovieDetailsFragment.newInstance(
                movie
            ), MovieDetailsFragment.TAG
        )
    }

    private fun displayFragment(fragment: Fragment, tag: String) {
        if (supportFragmentManager.findFragmentByTag(tag) != null) return

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.flContainer, fragment, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss()
    }
}
