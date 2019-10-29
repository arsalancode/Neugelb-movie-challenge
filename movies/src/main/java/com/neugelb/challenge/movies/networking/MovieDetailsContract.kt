package com.neugelb.challenge.movies.networking

import com.neugelb.challenge.movies.model.MovieDetailsModel
import io.reactivex.Single

interface MovieDetailsContract {

    interface Repo {
        /**
         * Get movie details [MovieDetailsModel] from the passed url
         * */

        fun getMovieDetails(movieId: Int, apiKey: String): Single<MovieDetailsModel>

    }

}