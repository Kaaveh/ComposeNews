package ir.kaaveh.base

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import ir.kaaveh.designsystem.widget.ErrorView
import ir.kaaveh.designsystem.widget.LoadingView

@Composable
fun BaseRoute(
    baseViewModel: BaseViewModel,
    content: @Composable () -> Unit,
) {

    val (baseState, baseEffect, baseEvent)
            = useBase(viewModel = baseViewModel)

    val context = LocalContext.current
    val activity = context as? Activity

    baseEffect.collectInLaunchedEffect { effect ->
        when (effect) {
            BaseContract.BaseEffect.OnBackPressed -> {
                activity?.onBackPressed()
            }
        }
    }

    BaseScreen(
        baseState = baseState,
        content = content,
    )

}

@Composable
private fun BaseScreen(
    baseState: BaseContract.BaseState,
    content: @Composable () -> Unit,
) {

    Box(modifier = Modifier.fillMaxSize()) {
        when (baseState) {
            BaseContract.BaseState.OnLoading -> {
                LoadingView(modifier = Modifier.fillMaxSize())
            }

            BaseContract.BaseState.OnLoadingDialog -> TODO()
            is BaseContract.BaseState.OnError -> {
                ErrorView(errorMessage = baseState.errorMessage)
            }

            BaseContract.BaseState.OnSuccess -> content()
        }
    }
}