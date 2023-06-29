package com.github.mohammadsianaki.core_test

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {
    val ui: CoroutineDispatcher
    val io: CoroutineDispatcher
    val bg: CoroutineDispatcher
}