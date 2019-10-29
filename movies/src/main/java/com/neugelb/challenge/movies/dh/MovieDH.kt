package com.neugelb.challenge.movies.dh

import com.neugelb.challenge.movies.networking.*
import com.neugelb.challenge.movies.viewmodel.MovieDetailsVM
import com.neugelb.challenge.movies.viewmodel.MovieSearchVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

/*Dependency holder for Movies module*/
object MovieDH {

    fun init() {
        loadKoinModules(
             movieDetailsModule(),
             movieSearchModule(),
             movieModule()
        )
    }

    //Details module
    private fun movieDetailsModule(): Module = module {
        viewModel { MovieDetailsVM(get()) }
        single {
             movieDetailsContract(
                get()
            )
        }
    }

    private fun movieDetailsContract(service: MovieService)
            :  MovieDetailsContract.Repo =
         MovieDetailsRepo(service)

    //Search module
    private fun movieSearchModule(): Module = module {
        viewModel { MovieSearchVM(get()) }
        single {
             movieSearchContract(
                get()
            )
        }
    }

    private fun movieSearchContract(service:  MovieService)
            :  MovieSearchContract.Repo =
         MovieSearchRepo(service)


    //Movie module
    private fun movieModule(): Module = module {
        single {  movieService(get()) }
    }

    private fun movieService(retrofit: Retrofit):  MovieService = retrofit.create()

}