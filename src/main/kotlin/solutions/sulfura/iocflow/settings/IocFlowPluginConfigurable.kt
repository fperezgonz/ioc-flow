package solutions.sulfura.iocflow.settings

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class IocFlowPluginConfigurable : Configurable {

    private var settingsComponent: IocFlowPluginSettingsComponent? = null

    override fun createComponent(): JComponent? {
        settingsComponent = IocFlowPluginSettingsComponent()
        return settingsComponent?.panel
    }

    override fun isModified(): Boolean {
        return settingsComponent?.isModified() ?: false
    }

    override fun apply() {
        settingsComponent?.apply()
    }

    override fun reset() {
        settingsComponent?.reset()
    }

    override fun disposeUIResources() {
        settingsComponent = null
    }

    override fun getDisplayName(): String {
        return "IoC Flow"
    }
}