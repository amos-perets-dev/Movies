package com.example.movies.modules

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies.R
import com.example.movies.movie_app.recyler_view.MovieLayoutManager
import com.example.movies.movie_app.shared.ISharedPerf
import com.example.movies.movie_app.shared.SharedPerf
import com.example.movies.network.base.BaseNetworkManager
import com.example.movies.network.base.IBaseNetworkManager
import com.example.movies.network.movies.manager.IMoviesNetworkManager
import com.example.movies.network.movies.manager.MoviesNetworkManager
import com.example.movies.network.movies.validator.IValidator
import com.example.movies.network.movies.validator.Validator
import com.example.movies.repo.fav_movies.IMoviesFavoritesRepository
import com.example.movies.repo.fav_movies.MoviesFavoritesRepository
import com.example.movies.repo.images.IImagesRepository
import com.example.movies.repo.images.ImagesRepository
import com.example.movies.repo.main_movies.IMoviesRepository
import com.example.movies.repo.main_movies.MoviesRepository
import com.example.movies.screen.base_movies_activity.MoviesListAdapter
import com.example.movies.screen.movie_details.MovieDetailsViewModel
import com.example.movies.screen.movies_fav_list.MoviesFavListActivity
import com.example.movies.screen.movies_fav_list.MoviesFavListViewModel
import com.example.movies.screen.movies_list.MoviesListActivity
import com.example.movies.screen.movies_list.MoviesListViewModel
import com.example.movies.screen.splash.SplashViewModel
import com.example.movies.utils.ImageUtil
import com.example.movies.repo.images.LoaderImageHelper
import com.example.movies.utils.network.NetworkConnectivityHelper
import io.realm.Realm
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

class ModulesMovie {

    fun createModules(context: Context): List<Module> {

        val applicationModule = module {
            factory<ISharedPerf> { SharedPerf() }
            factory { ImageUtil() }

            factory<IValidator> { Validator(get()) }

            single<IBaseNetworkManager> { BaseNetworkManager() }

            single<IImagesRepository> {
                ImagesRepository(
                    get(),
                    get(),
                    get(),
                    Realm.getDefaultInstance(),
                    LoaderImageHelper(),
                    context
                )
            }

            single<IMoviesNetworkManager> {
                MoviesNetworkManager(
                    get(),
                    get(),
                    get()
                )
            }


            single<IMoviesRepository> {
                MoviesRepository(
                    get(),
                    get<IImagesRepository>().getImagesAsync()
                )
            }

            single { MoviesListAdapter() }

            factory {
                MovieLayoutManager(
                    context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            }
        }


        val moduleSplash = module {

            viewModel {
                SplashViewModel(get(), get(), NetworkConnectivityHelper(context))
            }

        }

        val moduleMoviesList = module {

            viewModel {
                MoviesListViewModel(
                    get<IMoviesRepository>().getMoviesData(),
                    R.string.activity_movies_list_button_my_list_text,
                    Intent(context, MoviesFavListActivity::class.java)
                )
            }
        }

        val moduleFavorites = module {

            single<IMoviesFavoritesRepository> { MoviesFavoritesRepository() }

            viewModel {
                MoviesFavListViewModel(
                    get<IMoviesFavoritesRepository>().getMoviesData(),
                    R.string.activity_my_movies_list_button_movies_list_text,
                    Intent(context, MoviesListActivity::class.java)
                )
            }

        }

        val moduleMovieDetails = module {

            viewModel {
                MovieDetailsViewModel(get(), get(), get<MoviesListAdapter>().getItemClicked())
            }
        }

        return listOf(
            applicationModule,
            moduleSplash,
            moduleMoviesList,
            moduleMovieDetails,
            moduleFavorites
        )

    }


}