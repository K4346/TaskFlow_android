package com.example.taskflow.di

import com.example.taskflow.data.repositories.AccountRepositoryImpl
import com.example.taskflow.data.repositories.LogRepositoryImpl
import com.example.taskflow.domain.repositories.AccountRepository
import com.example.taskflow.domain.repositories.LogRepository
import com.example.taskflow.domain.use_cases.UserInfoUseCase
import com.example.taskflow.domain.use_cases.UserInfoUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun provideAccountService(impl: AccountRepositoryImpl): AccountRepository


    @Binds
    @Singleton
    abstract fun provideUserInfoUseCase(impl: UserInfoUseCaseImpl): UserInfoUseCase

    @Binds
    @Singleton
    abstract fun provideLogService(impl: LogRepositoryImpl): LogRepository

}
