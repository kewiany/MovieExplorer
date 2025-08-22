package xyz.kewiany.movieexplorer.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xyz.kewiany.movieexplorer.data.SearchTextHolder
import xyz.kewiany.movieexplorer.data.api.TMDBApi
import xyz.kewiany.movieexplorer.data.dao.FavoriteDao
import xyz.kewiany.movieexplorer.data.dao.MovieDao
import xyz.kewiany.movieexplorer.data.dao.SearchHistoryDao
import xyz.kewiany.movieexplorer.data.repository.FavoriteMovieRepositoryImpl
import xyz.kewiany.movieexplorer.data.repository.MovieRepositoryImpl
import xyz.kewiany.movieexplorer.data.repository.SearchHistoryRepositoryImpl
import xyz.kewiany.movieexplorer.data.repository.SearchRepositoryImpl
import xyz.kewiany.movieexplorer.domain.repository.FavoriteMovieRepository
import xyz.kewiany.movieexplorer.domain.repository.MovieRepository
import xyz.kewiany.movieexplorer.domain.repository.SearchHistoryRepository
import xyz.kewiany.movieexplorer.domain.repository.SearchRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideMovieRepository(
        movieDao: MovieDao,
        api: TMDBApi,
    ): MovieRepository = MovieRepositoryImpl(movieDao, api)

    @Provides
    @Singleton
    fun provideFavoriteRepository(
        favoriteDao: FavoriteDao,
    ): FavoriteMovieRepository = FavoriteMovieRepositoryImpl(favoriteDao)

    @Singleton
    @Provides
    fun provideSearchRepository(
        searchTextHolder: SearchTextHolder,
    ): SearchRepository = SearchRepositoryImpl(searchTextHolder)

    @Provides
    @Singleton
    fun provideSearchHistoryRepository(
        dao: SearchHistoryDao,
    ): SearchHistoryRepository = SearchHistoryRepositoryImpl(dao)
}


