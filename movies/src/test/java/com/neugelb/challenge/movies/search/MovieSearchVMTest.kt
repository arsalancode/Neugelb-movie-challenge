package com.neugelb.challenge.movies.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.neugelb.challenge.BuildConfig
import com.neugelb.challenge.movies.model.MovieSearchModel
import com.neugelb.challenge.movies.utils.TrampolineSchedulerRule
import com.neugelb.challenge.movies.viewmodel.MovieSearchVM
import com.neugelb.challenge.networking.RemoteResponse
import com.neugelb.challenge.utils.TestingUtils
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.lang.reflect.Type

@RunWith(RobolectricTestRunner::class)
class MovieSearchVMTest {

    //Ensure livedata executions happen immediately
    @get:Rule
    var ruleInstant = InstantTaskExecutorRule()

    //Ensure rx executions happen immediately
    @get:Rule
    var ruleRx = TrampolineSchedulerRule()

    //Class to test
    private lateinit var viewModel: MovieSearchVM
    //Mock the repo
    private val repo = mock<com.neugelb.challenge.movies.networking.MovieSearchContract.Repo>()

    //Mock the loading and error observers
    private val loadingObs = mock<Observer<Boolean>>()
    private val errorObs = mock<Observer<Throwable>>()

    //Mock the pagination and Movie list observers
    private val paginationObs = mock<Observer<Boolean>>()
    private val MoviesObs = mock<Observer<List<MovieSearchModel>>>()

    private val gson = Gson()

    private val initialSuccessData = mockInitialData()

    @Before
    fun setUp() {
        //Setup the viewmodel with mocked repo and test scheduler
        viewModel = MovieSearchVM(repo = repo)

        //Attach observers to loading and error
        viewModel.loading.observeForever(loadingObs)
        viewModel.error.observeForever(errorObs)

        //Attach observers to pagination and moviesNowPlaying list
        viewModel.paginationLoading.observeForever(paginationObs)
        viewModel.movies.observeForever(MoviesObs)

    }

    @Test
    fun testInitialLoad() {
        //Mock success data response from repo
        whenever(repo.moviesNowPlaying(BuildConfig.API_KEY, 1)).doReturn(Single.just(initialSuccessData))

        //Trigger the load
        viewModel.initialLoad()

        //Verify loading state changes
        verify(loadingObs).onChanged(true)

        //Verify correct methods are called in the repo
        verify(repo).moviesNowPlaying(BuildConfig.API_KEY, 1)

        //Verify loading state changes again
        verify(loadingObs).onChanged(false)

        //Verify data pushed to UI
        verify(MoviesObs).onChanged(any())

        //Verify error is never pushed to the UI
        verify(errorObs, never()).onChanged(any())

        //Verify the moviesNowPlaying are reset and not appended
        assertEquals(20, viewModel.movies.value?.size)

        pm("Initial load calls the correct functions in the repo and sets success data correctly")
    }

    @Test
    fun testLoadNextValid() {
        //Mock success data response from repo
        whenever(repo.moviesNowPlaying(BuildConfig.API_KEY, 1)).doReturn(Single.just(initialSuccessData))

        //Setup
        viewModel.setMovie(initialSuccessData.results)
        val nextUrl = initialSuccessData.page
        //Set valid page page url
        viewModel.setNextPageUrl(nextUrl - 1)

        //Trigger page load
        viewModel.loadNextPage()

        //Verify pagination state changes
        verify(paginationObs).onChanged(true)

        //Verify correct methods are called in the repo
        verify(repo).moviesNowPlaying(BuildConfig.API_KEY, 1)

        //Verify pagination state changes again
        verify(paginationObs).onChanged(false)

        //Verify data pushed to UI
        verify(MoviesObs, atLeastOnce()).onChanged(any())

        //Verify error is never pushed to the UI
        verify(errorObs, never()).onChanged(any())

        //Verify the moviesNowPlaying are not reset and appended instead
        assertEquals(40, viewModel.movies.value?.size)

        pm("Load page with a valid page page url calls the correct functions in the repo and appends success data correctly")
    }

    @Test
    fun testSearchMovie() {
        val queriedMovie = "it"
        //Mock success data response from repo
        whenever(repo.searchMovie(BuildConfig.API_KEY, 1, queriedMovie)).doReturn(Single.just(mockItSearch()))

        //Setup
        viewModel.setMovie(initialSuccessData.results)

        //Trigger search
        viewModel.searchMovie(queriedMovie)

        //Verify loading state changes
        verify(loadingObs).onChanged(true)

        //Verify correct methods are called in the repo
        verify(repo).searchMovie(BuildConfig.API_KEY, 1, queriedMovie)

        //Verify loading state changes again
        verify(loadingObs).onChanged(false)

        //Verify data pushed to UI
        verify(MoviesObs, atLeastOnce()).onChanged(any())

        //Verify error is never pushed to the UI
        verify(errorObs, never()).onChanged(any())

        //Verify the moviesNowPlaying are reset and not appended
        assertEquals(1, viewModel.movies.value?.size)

        pm("Search for a movie triggers correct functions in the repo and resets the results to the UI")
    }

    @Test
    fun testRefresh() {
        //Mock success data response from repo
        whenever(repo.moviesNowPlaying(BuildConfig.API_KEY, 1)).doReturn(Single.just(initialSuccessData))

        //Trigger the load
        viewModel.refreshMovies()

        //Verify loading state changes
        verify(loadingObs).onChanged(true)

        //Verify correct methods are called in the repo
        verify(repo).moviesNowPlaying(BuildConfig.API_KEY, 1)

        //Verify loading state changes again
        verify(loadingObs).onChanged(false)

        //Verify data pushed to UI
        verify(MoviesObs).onChanged(any())

        //Verify error is never pushed to the UI
        verify(errorObs, never()).onChanged(any())

        //Verify the moviesNowPlaying are reset and not appended
        assertEquals(20, viewModel.movies.value?.size)

        pm("Refresh calls the correct functions in the repo and sets success data correctly")
    }


    //region region: Utils
    private fun pm(message: String) {
        println("\nMovie search verified: $message")
    }

    private fun mockInitialData(): RemoteResponse<List<MovieSearchModel>> {
        val responseModelToken: Type = object : TypeToken<RemoteResponse<List<MovieSearchModel>>>() {}.type
        return gson.fromJson(
            TestingUtils.getResponseFromJson("/search/initial_load"),
            responseModelToken
        )
    }

    private fun mockItSearch(): RemoteResponse<List<MovieSearchModel>> {
        val responseModelToken: Type = object : TypeToken<RemoteResponse<List<MovieSearchModel>>>() {}.type
        return gson.fromJson(
            TestingUtils.getResponseFromJson("/search/it_search"),
            responseModelToken
        )
    }

    //endregion

}