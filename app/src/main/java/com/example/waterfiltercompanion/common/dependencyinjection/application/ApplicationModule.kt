package com.example.waterfiltercompanion.common.dependencyinjection.application

import android.content.Context
import com.example.waterfiltercompanion.Application
import com.example.waterfiltercompanion.common.dependencyinjection.DiConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Named(DiConstants.APPLICATION_CONTEXT)
    fun provideApplicationContext(application: Application): Context = application
}