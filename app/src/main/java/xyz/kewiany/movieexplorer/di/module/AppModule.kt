package xyz.kewiany.movieexplorer.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xyz.kewiany.movieexplorer.data.SearchTextHolder
import xyz.kewiany.movieexplorer.data.SearchTextHolderImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module(includes = [AppModule.BindsModule::class])
object AppModule {

    @InstallIn(SingletonComponent::class)
    @Module
    interface BindsModule {
        @Singleton
        @Binds
        fun bindsSearchTextHolder(impl: SearchTextHolderImpl): SearchTextHolder
    }
}