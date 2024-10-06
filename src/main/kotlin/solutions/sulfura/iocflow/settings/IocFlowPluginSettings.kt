package solutions.sulfura.iocflow.settings

import com.intellij.openapi.components.*

@State(name = "IocFlowPluginSettings", storages = [Storage("IocFlowPluginSettings.xml")])
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