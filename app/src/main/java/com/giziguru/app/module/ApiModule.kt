package com.giziguru.app.module

import android.app.Application
import android.content.Context
import com.giziguru.app.data.remote.retrofit.ApiConfig
import com.giziguru.app.data.remote.retrofit.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    @Singleton
    fun provideApiService(): ApiService = ApiConfig.getApiService()

    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }
}