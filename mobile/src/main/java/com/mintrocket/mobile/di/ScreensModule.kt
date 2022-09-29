package com.mintrocket.mobile.di

import com.mintrocket.mobile.screens.contentlist.adapter.ContentItemsBuilder
import org.koin.dsl.module

val screensModule = module {

    factory { ContentItemsBuilder() }
}