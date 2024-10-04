package solutions.sulfura.errorbuilderplugin.menu

import com.intellij.openapi.components.*

@State(name = "ErrorBuilderPluginSettings", storages = [Storage("ErrorBuilderPluginSettings.xml")])
@Service
class ErrorBuilderPluginSettings : PersistentStateComponent<ErrorBuilderPluginSettings.State> {
    private var state = State()

    companion object {
        val instance: ErrorBuilderPluginSettings
            get() = service()
    }

    data class State(
        var intentionName: String = "errorBuilder",
        var injectionClass: String = "solutions.sulfura.errorbuilderplugin.ErrorBuilder"
    )

    override fun getState(): State {
        return state
    }

    override fun loadState(state: State) {
        this.state = state
    }

    fun getIntentionName(): String {
        return state.intentionName
    }

    fun setIntentionName(intentionName: String) {
        state.intentionName = intentionName
    }

    fun getInjectionClass(): String {
        return state.injectionClass
    }

    fun setInjectionClass(injectionClass: String) {
        state.injectionClass = injectionClass
    }
}