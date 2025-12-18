package com.example.auth.presetation.di

import com.example.auth.presetation.login.LoginViewModel
import com.example.auth.presetation.register.RegisterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authViewModule = module {
   viewModelOf(::RegisterViewModel)
   viewModelOf(::LoginViewModel)
}