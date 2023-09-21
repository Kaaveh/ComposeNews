package ir.composenews.base

interface BaseContract :
    BaseUnidirectionalViewModel<BaseContract.BaseEvent, BaseContract.BaseEffect, BaseContract.BaseState> {

    sealed class BaseState {
        data object OnLoading : BaseState()
        data object OnLoadingDialog : BaseState()
        data class OnError(val errorMessage: String) : BaseState()
        data object OnSuccess : BaseState()
    }

    sealed class BaseEffect {
        data object OnBackPressed : BaseEffect()
    }

    sealed class BaseEvent {
        data object OnBackPressed : BaseEvent()
        data object OnRetryPressed : BaseEvent()
    }
}
