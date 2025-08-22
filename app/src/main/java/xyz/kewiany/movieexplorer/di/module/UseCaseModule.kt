package xyz.kewiany.movieexplorer.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xyz.kewiany.movieexplorer.domain.repository.FavoriteMovieRepository
import xyz.kewiany.movieexplorer.domain.repository.MovieRepository
import xyz.kewiany.movieexplorer.domain.repository.SearchHistoryRepository
import xyz.kewiany.movieexplorer.domain.repository.SearchRepository
import xyz.kewiany.movieexplorer.domain.usecase.ClearSearchTextUseCase
import xyz.kewiany.movieexplorer.domain.usecase.FetchOverviewUseCase
import xyz.kewiany.movieexplorer.domain.usecase.FetchReleaseDateUseCase
import xyz.kewiany.movieexplorer.domain.usecase.FetchSpokenLanguagesUseCase
import xyz.kewiany.movieexplorer.domain.usecase.GetMovieDetailsUseCase
import xyz.kewiany.movieexplorer.domain.usecase.GetMoviesPagedUseCase
import xyz.kewiany.movieexplorer.domain.usecase.GetSearchHistoryUseCase
import xyz.kewiany.movieexplorer.domain.usecase.GetSearchTextUseCase
import xyz.kewiany.movieexplorer.domain.usecase.SearchMoviesUseCase
import xyz.kewiany.movieexplorer.domain.usecase.SetSearchTextUseCase
import xyz.kewiany.movieexplorer.domain.usecase.ToggleFavoriteMovieUseCase

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideSearchMoviesUseCase(repository: MovieRepository, favoriteRepository: FavoriteMovieRepository) =
        SearchMoviesUseCase(repository, favoriteRepository)

    @Provides
    fun provideGetMovieDetailsUseCase(repository: MovieRepository) = GetMovieDetailsUseCase(repository)

    @Provides
    fun provideFetchOverviewUseCase(repository: MovieRepository) = FetchOverviewUseCase(repository)

    @Provides
    fun provideFetchReleaseDateUseCase(repository: MovieRepository) = FetchReleaseDateUseCase(repository)

    @Provides
    fun provideFetchSpokenLanguagesUseCase(repository: MovieRepository) = FetchSpokenLanguagesUseCase(repository)

    @Provides
    fun provideToggleFavoriteMovieUseCase(repository: FavoriteMovieRepository) = ToggleFavoriteMovieUseCase(repository)

    @Provides
    fun provideGetMoviesPagedUseCase(movieRepository: MovieRepository, favoriteRepository: FavoriteMovieRepository) =
        GetMoviesPagedUseCase(movieRepository, favoriteRepository)

    @Provides
    fun provideSetSearchTextUseCase(searchRepository: SearchRepository): SetSearchTextUseCase =
        SetSearchTextUseCase(searchRepository)

    @Provides
    fun provideClearSearchTextUseCase(searchRepository: SearchRepository): ClearSearchTextUseCase =
        ClearSearchTextUseCase(searchRepository)

    @Provides
    fun provideGetSearchTextUseCase(searchRepository: SearchRepository): GetSearchTextUseCase =
        GetSearchTextUseCase(searchRepository)

    @Provides
    fun provideGetSearchHistoryUseCase(
        historyRepository: SearchHistoryRepository,
        favoriteRepository: FavoriteMovieRepository,
    ): GetSearchHistoryUseCase = GetSearchHistoryUseCase(historyRepository, favoriteRepository)
}

