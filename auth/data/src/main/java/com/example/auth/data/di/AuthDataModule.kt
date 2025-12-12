package com.example.auth.data.di

import com.example.auth.data.AuthRepositoryImpl
import com.example.auth.data.EmailPatternValidator
import com.example.auth.domain.AuthRepository
import com.example.auth.domain.PatternValidator
import com.example.auth.domain.UserDataValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
    single<PatternValidator> {
        EmailPatternValidator
    }

    singleOf(::UserDataValidator)

    // The bind is necessary to tell koin to inject AuthRepositoryImpl to every class that depends on `AuthRepository` interface
    // i.e. the RegisterViewModel
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
}