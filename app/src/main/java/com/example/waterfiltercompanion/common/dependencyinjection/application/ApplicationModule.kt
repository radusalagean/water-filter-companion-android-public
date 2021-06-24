package com.example.waterfiltercompanion.common.dependencyinjection.application

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.waterfiltercompanion.common.date.DateHelper
import com.example.waterfiltercompanion.common.dependencyinjection.DiConstants
import com.example.waterfiltercompanion.datapersistence.LocalRepository
import com.example.waterfiltercompanion.watercontrol.ConsumeWaterUseCase
import dagger.Module
import dagger.Provides
import dagger.Reusable
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

    @Provides
    @Reusable
    fun provideDateHelper() = DateHelper()

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @Named(DiConstants.APPLICATION_CONTEXT) context: Context
    ): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    @Reusable
    fun provideLocalRepository(
        sharedPreferences: SharedPreferences
    ): LocalRepository = LocalRepository(sharedPreferences)

    @Provides
    @Reusable
    fun provideConsumeWaterUseCase(
        localRepository: LocalRepository
    ): ConsumeWaterUseCase = ConsumeWaterUseCase(localRepository)
}