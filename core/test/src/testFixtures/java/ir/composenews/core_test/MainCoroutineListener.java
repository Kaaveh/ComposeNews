package ir.composenews.core_test;

import io.kotest.core.listeners.AfterSpecListener;
import io.kotest.core.listeners.BeforeSpecListener;
import io.kotest.core.spec.Spec;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.test.TestCoroutineDispatchersKt;
import kotlinx.coroutines.test.TestDispatcher;
import kotlinx.coroutines.test.TestDispatchers;

public final class MainCoroutineListener implements BeforeSpecListener, AfterSpecListener {
    private final TestDispatcher testDispatcher;

    public MainCoroutineListener() {
        this(TestCoroutineDispatchersKt.UnconfinedTestDispatcher(null, null));
    }

    public MainCoroutineListener(TestDispatcher testDispatcher) {
        this.testDispatcher = testDispatcher;
    }

    @Override
    public Object beforeSpec(Spec spec, Continuation<? super Unit> continuation) {
        TestDispatchers.setMain(Dispatchers.INSTANCE, testDispatcher);
        return Unit.INSTANCE;
    }

    @Override
    public Object afterSpec(Spec spec, Continuation<? super Unit> continuation) {
        TestDispatchers.resetMain(Dispatchers.INSTANCE);
        return Unit.INSTANCE;
    }
}
