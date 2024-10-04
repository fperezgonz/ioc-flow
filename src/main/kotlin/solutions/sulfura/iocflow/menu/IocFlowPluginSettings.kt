package solutions.sulfura.iocflow.menu

import com.intellij.openapi.components.*

@State(name = "ErrorBuilderPluginSettings", storages = [Storage("ErrorBuilderPluginSettings.xml")])
@Service
class IocFlowPluginSettings : PersistentStateComponent<IocFlowPluginSettings.State> {

    private var state = State()

    companion object {
        val instance: IocFlowPluginSettings
            get() = service()
    }

    data class State(
        var intentDefinitions: List<IntentDefinition> = arrayListOf()
    )

    data class IntentDefinition(
        var intentionName: String = "",
        var packageName: String = "",
        var injectionClass: String = "",
        var fieldName: String? = null
    )

    override fun getState(): State {
        return state
    }

    override fun loadState(state: State) {
        this.state = state
    }

}