package com.example.remotelist

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@HiltAndroidApp
class RemoteListApplication : Application()

@Module
@InstallIn(SingletonComponent::class)
object ViewModelObject{

    @Provides
    fun provideContext(@ApplicationContext context: Context) = context

}