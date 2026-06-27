package ir.composenews.core_test.dispatcher;

import ir.composenews.base.dispatcher.DispatcherProvider;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.test.TestCoroutineDispatchersKt;
import kotlinx.coroutines.test.TestCoroutineScheduler;
import kotlinx.coroutines.test.TestDispatcher;

public final class TestDispatcherProvider implements DispatcherProvider {
    private final TestDispatcher ui;
    private final TestDispatcher io;
    private final TestDispatcher bg;

    public TestDispatcherProvider()  {
       this(new TestCoroutineScheduler());
    }

    public TestDispatcherProvider(TestCoroutineScheduler testScheduler) {
        ui = TestCoroutineDispatchersKt.UnconfinedTestDispatcher(testScheduler, null);
        io = TestCoroutineDispatchersKt.UnconfinedTestDispatcher(testScheduler, null);
        bg = TestCoroutineDispatchersKt.UnconfinedTestDispatcher(testScheduler, null);
    }

    @Override
    public CoroutineDispatcher getUi() {
        return ui;
    }

    @Override
    public CoroutineDispatcher getIo() {
        return io;
    }

    @Override
    public CoroutineDispatcher getBg() {
        return bg;
    }
}
