package com.neugelb.challenge.movies.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.neugelb.challenge.BuildConfig
import com.neugelb.challenge.movies.model.GenresModel
import com.neugelb.challenge.movies.model.MovieDetailsModel
import com.neugelb.challenge.movies.networking.MovieDetailsContract
import com.neugelb.challenge.movies.utils.TrampolineSchedulerRule
import com.neugelb.challenge.movies.viewmodel.MovieDetailsVM
import com.neugelb.challenge.utils.TestingUtils
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.lifecycle.Observer
//import com.google.gson.Gson
//import com.neugelb.challenge.BuildConfig
//import com.neugelb.challenge.movies.model.*
//import com.neugelb.challenge.movies.networking.MovieDetailsContract
//import com.neugelb.challenge.movies.utils.TrampolineSchedulerRule
//import com.neugelb.challenge.movies.viewmodel.MovieDetailsVM
//import com.neugelb.challenge.utils.TestingUtils
//import com.nhaarman.mockitokotlin2.doReturn
//import com.nhaarman.mockitokotlin2.mock
//import com.nhaarman.mockitokotlin2.whenever
//import io.reactivex.Single
//import org.junit.Assert.assertEquals
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.robolectric.RobolectricTestRunner
//



@RunWith(RobolectricTestRunner::class)
class MovieDetailsVMTest {

    //Ensure livedata executions happen immediately
    @get:Rule
    var ruleInstant = InstantTaskExecutorRule()

    //Ensure rx executions happen immediately
    @get:Rule
    var ruleRx = TrampolineSchedulerRule()

    //Class to test
    private lateinit var viewModel: MovieDetailsVM
    //Mock the repo
    private val repo = mock<MovieDetailsContract.Repo>()

    //Mock the loading and error observers
    private val loadingObs = mock<Observer<Boolean>>()
    private val errorObs = mock<Observer<Throwable>>()
    private val gson = Gson()

    private val baseUrl = BuildConfig.API_BASE //"https://swapi.co/api/"
//    private val characterUrl = "${baseUrl}people/1/"
//    private val speciesUrl = "${baseUrl}species/1/"
//    private val homeworldUrl = "${baseUrl}planets/9/"
//    private val filmUrl2 = "${baseUrl}films/2/"
//    private val filmUrl6 = "${baseUrl}films/6/"

    private val movieId = 475557

    @Before
    fun setUp() {
        //Setup the viewmodel with mocked repo and test scheduler
        viewModel = MovieDetailsVM(repo = repo)

        //Attach observers to loading and error
        viewModel.loading.observeForever(loadingObs)
        viewModel.error.observeForever(errorObs)
    }

    @Test
    fun testSuccessfulRemoteLoad() {
        //Mock success data response from repo
//        whenever(repo.getMovieDetails(movieId, BuildConfig.API_KEY).doReturn(Single.just(mockMovieDetails())))

        //Create a test observer for details
        val detailsObs = mock<Observer<MovieDetailsModel>>()

        var genres = mutableListOf<GenresModel>()
        genres.add(GenresModel(80, "Crime"))
        genres.add(GenresModel(53, "Thriller"))
        genres.add(GenresModel(18, "Drama"))

        val expectedDetails = MovieDetailsModel(
            id = 475557,
            title = "Joker",
            adult = false,
            overview = "During the 1980s, a failed stand-up comedian is driven insane and turns to a life of crime and chaos in Gotham City while becoming an infamous psychopathic crime figure.",
            poster_path = "/udDclJoHjfjb8Ekgsd4FDteOkCU.jpg",
            coverImagePath = "/n6bUvigpRFqSwmPp1m2YADdbRBc.jpg",
            vote_average = 8.6,
            runtime = 122,
            runTime = "2h 2m",
            release_date = "2019-10-02",
            releaseDate = "02 Oct 2019",
            genres = genres,
            genresList = "Crime, Thriller, Drama"
        )

        //Trigger the load and attach the observer
        viewModel.getMovieDetails(movieId).observeForever(detailsObs)

        //Verify loading state changes
        verify(loadingObs).onChanged(true)

        //Verify correct methods are called in the repo
        verify(repo).getMovieDetails(movieId, BuildConfig.API_KEY)

        //Verify loading state changes again
        verify(loadingObs).onChanged(false)

        //Verify data pushed to UI is the same we expected
        verify(detailsObs).onChanged(expectedDetails)

        //Verify error is never pushed to the UI
        verify(errorObs, never()).onChanged(any())

        pm("We fire API calls properly based on the movie details and pushed the result correctly to the UI.")
    }

    @Test
    fun testPartialRemoteLoad() {
        //Mock success data response from repo
        whenever(repo.getMovieDetails(movieId, BuildConfig.API_KEY)).doReturn(Single.just(mockMovieDetails()))

        //Create a test observer for details
        val detailsObs = mock<Observer< MovieDetailsModel>>()

        var genres = mutableListOf<GenresModel>()
        genres.add(GenresModel(80, "Crime"))
        genres.add(GenresModel(53, "Thriller"))
        genres.add(GenresModel(18, "Drama"))


        val expectedDetails =  MovieDetailsModel(
            id = 475557,
            title = "Joker",
            adult = false,
            overview = "During the 1980s, a failed stand-up comedian is driven insane and turns to a life of crime and chaos in Gotham City while becoming an infamous psychopathic crime figure.",
            poster_path = "/udDclJoHjfjb8Ekgsd4FDteOkCU.jpg",
            coverImagePath = "/n6bUvigpRFqSwmPp1m2YADdbRBc.jpg",
            vote_average = 8.6,
            runtime = 122,
            runTime = "2h 2m",
            release_date = "2019-10-02",
            releaseDate = "02 Oct 2019",
            genres = genres,
            genresList = "Crime, Thriller, Drama"
        )

        //Trigger the load and attach the observer
        viewModel.getMovieDetails(movieId).observeForever(detailsObs)

        //Verify loading state changes
        verify(loadingObs).onChanged(true)

        //Verify correct methods are called in the repo
        verify(repo).getMovieDetails(movieId, BuildConfig.API_KEY)

        //Verify loading state changes again
        verify(loadingObs).onChanged(false)

        //Verify data pushed to UI is the same we expected
        verify(detailsObs).onChanged(expectedDetails)

        //Verify error is never pushed to the UI
        verify(errorObs, never()).onChanged(any())

        pm("We fire API calls properly based on the movie details and pushed the result correctly to the UI.")
    }

    @Test
    fun testDateConversionValid() {
        val expected = "27 Sep 2019"
        assertEquals(expected, viewModel.formatDate("2019-09-27"))
        pm("Date is correct converted")
    }

    @Test
    fun testDateConversionInvalid() {
        assertEquals("", viewModel.formatDate("unknown"))
        pm("Date is correct handled for invalid values")
    }

    @Test
    fun testDateConversionEmpty() {
        assertEquals("", viewModel.formatDate(""))
        pm("Date is correct handled for empty values")
    }


    //region region: Utils
    private fun pm(message: String) {
        println("\nMovie details verified: $message")
    }

    private fun mockMovieDetails(): MovieDetailsModel {
        return gson.fromJson(
            TestingUtils.getResponseFromJson("/details/movie_details"),
            MovieDetailsModel::class.java
        )
    }

    //endregion

}