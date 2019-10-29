package com.neugelb.challenge.movies.networking

import com.neugelb.challenge.movies.model.MovieSearchModel
import com.neugelb.challenge.networking.RemoteResponse
import io.reactivex.Single

class MovieSearchRepo(private val service: MovieService) :
    MovieSearchContract.Repo {

    override fun moviesNowPlaying(apiKey: String, page: Int)
            : Single<RemoteResponse<List<MovieSearchModel>>> = service.getMoviesNowPlaying(apiKey, page)

    override fun searchMovie(apiKey: String, page: Int, query: String)
            : Single<RemoteResponse<List<MovieSearchModel>>> = service.searchMovie(apiKey, page, query)

}