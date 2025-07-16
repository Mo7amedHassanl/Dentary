package com.m7md7sn.dentary.di

import com.m7md7sn.dentary.data.repository.AuthRepository
import com.m7md7sn.dentary.data.repository.AuthRepositoryImpl
import com.m7md7sn.dentary.data.source.remote.AuthDataSource
import com.m7md7sn.dentary.data.source.remote.AuthDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthDataSource(
        authDataSourceImpl: AuthDataSourceImpl
    ): AuthDataSource

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}