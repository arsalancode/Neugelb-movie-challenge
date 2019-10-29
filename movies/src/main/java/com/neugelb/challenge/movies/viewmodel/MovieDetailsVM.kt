package com.neugelb.challenge.movies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.neugelb.challenge.BuildConfig
import com.neugelb.challenge.movies.model.GenresModel
import com.neugelb.challenge.movies.model.MovieDetailsModel
import com.neugelb.challenge.movies.networking.MovieDetailsContract
import com.neugelb.challenge.view.extensions.hide
import com.neugelb.challenge.view.extensions.show
import com.neugelb.challenge.viewmodel.BaseVM
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import kotlin.math.floor

class MovieDetailsVM(private val repo:  MovieDetailsContract.Repo) : BaseVM() {

    private val movieDetails = MutableLiveData<MovieDetailsModel>()

    fun getMovieDetails(movieId: Int): LiveData< MovieDetailsModel> {
        if (movieDetails.value == null) {

            _loading.show()


            repo.getMovieDetails(movieId, BuildConfig.API_KEY)
                //Subscribe on io thread
                .subscribeOn(Schedulers.io())
                //Move to computation thread
                .observeOn(Schedulers.computation())
                .map {
                    val runTime = convertTime(it.runtime)
                    val releaseDate = formatDate(it.release_date)
                    val genresList = getGenresList(it.genres)

                    return@map it.copy(runTime = runTime, releaseDate = releaseDate, genresList = genresList)
                }
                .subscribe({
                    _loading.hide()

                    //Pass complete movie details to UI
                    movieDetails.postValue(it)
                }, { handleError(it) })
                .addTo(disposable)
        }

        return movieDetails
    }

    private fun getGenresList(genres : List<GenresModel>?) : String{

        var genresList = StringBuilder("")

        genres?.forEachIndexed { i, genresModel ->
            genresList.append(genresModel.name)

            if (i+1 < genres.size)
                genresList.append(", ")

        }

        return genresList.toString()
    }

    fun formatDate( date: String? ) : String{

        return date.let {
            val parser = SimpleDateFormat("yyyy-MM-dd")
            val formatter = SimpleDateFormat("dd MMM yyyy")

            try {
                formatter.format(parser.parse(date))
            }catch ( exaction: Exception){
                return ""
            }
        }

    }

    private fun convertTime(time: Int?): String {

        return time?.let {
            var hours : Int = 0
            var minutes = time

            if (time >= 60 ) {
                hours = floor((it / 60).toDouble()).toInt()
                minutes = it - (hours * 60)
            }

            if (time == 0)
                ""
            else
                "${hours}h ${minutes}m"

        } ?: kotlin.run {
            return ""
        }
    }

}
