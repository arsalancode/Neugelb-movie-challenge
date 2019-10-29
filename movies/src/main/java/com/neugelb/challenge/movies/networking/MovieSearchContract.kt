package com.neugelb.challenge.movies.networking

import com.neugelb.challenge.movies.model.MovieSearchModel
import com.neugelb.challenge.networking.RemoteResponse
import io.reactivex.Single

interface MovieSearchContract {
    interface Repo {

        /**
         * Get all the moviesNowPlaying with pagination
         * */
        fun moviesNowPlaying(apiKey: String, page: Int): Single<RemoteResponse<List<MovieSearchModel>>>

        /**
         * Get searched moviesNowPlaying with pagination
         * */
//        fun searchMovie(query: String): Single<RemoteResponse<List<MovieSearchModel>>>
        fun searchMovie(apiKey: String, page: Int, query: String): Single<RemoteResponse<List<MovieSearchModel>>>

    }
}