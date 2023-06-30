package ir.kaaveh.core_test

import ir.kaaveh.core_test.dispatcher.DispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val dispatcherProvider: DispatcherProvider
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcherProvider.ui)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
