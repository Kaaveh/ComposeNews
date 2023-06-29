package com.github.mohammadsianaki.core_test

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class PlatformDispatcherProvider @Inject constructor() : DispatcherProvider {
    override val ui: CoroutineDispatcher = Dispatchers.Main
    override val io: CoroutineDispatcher = Dispatchers.IO
    override val bg: CoroutineDispatcher = Dispatchers.Default
}