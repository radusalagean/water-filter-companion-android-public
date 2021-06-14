package com.example.waterfiltercompanion.common.dependencyinjection.activity

import android.app.Activity
import android.content.Context
import com.example.waterfiltercompanion.common.dependencyinjection.DiConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Named

@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {

    @Provides
    @Named(DiConstants.ACTIVITY_CONTEXT)
    fun provideActivityContext(activity: Activity): Context = activity
}