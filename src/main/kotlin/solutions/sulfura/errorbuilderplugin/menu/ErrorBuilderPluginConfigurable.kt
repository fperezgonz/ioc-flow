package solutions.sulfura.errorbuilderplugin.menu

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class ErrorBuilderPluginConfigurable : Configurable {

    private var settingsComponent: ErrorBuilderPluginSettingsComponent? = null

    override fun createComponent(): JComponent? {
        settingsComponent = ErrorBuilderPluginSettingsComponent()
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
        return "IntentionsPlugin"
    }
}