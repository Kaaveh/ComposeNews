package ir.kaaveh.designsystem.base

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import ir.kaaveh.designsystem.collectInLaunchedEffect
import ir.kaaveh.designsystem.useBase
import ir.kaaveh.designsystem.widget.LoadingView

@Composable
fun BaseScreen(
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

    Box(modifier = Modifier.fillMaxSize()) {
        when (baseState) {
            BaseContract.BaseState.OnLoading -> {
                LoadingView()
            }
            BaseContract.BaseState.OnLoadingDialog -> TODO()
            is BaseContract.BaseState.OnError -> TODO()
            BaseContract.BaseState.OnSuccess -> content()
        }
    }
}