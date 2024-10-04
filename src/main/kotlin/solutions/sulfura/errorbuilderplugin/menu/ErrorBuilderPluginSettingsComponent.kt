package solutions.sulfura.errorbuilderplugin.menu

import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class ErrorBuilderPluginSettingsComponent {
    val panel: JPanel = JPanel() // Implement your layout here
    private val intentionNameField: JTextField = JTextField()
    private val intentionNameLabel: JLabel = JLabel("IntentionName");
    private val injectionClassLabel: JLabel = JLabel("InjectionClass");
    private val injectionClassField: JTextField = JTextField()

    init {
        // Add your fields to the panel layout
        panel.add(intentionNameLabel)
        panel.add(intentionNameField)
        panel.add(injectionClassLabel)
        panel.add(injectionClassField)
    }

    fun isModified(): Boolean {
        return intentionNameField.text != ErrorBuilderPluginSettings.instance.getIntentionName() ||
                injectionClassField.text != ErrorBuilderPluginSettings.instance.getInjectionClass()
    }

    fun apply() {
        intentionNameField.text?.let { ErrorBuilderPluginSettings.instance.setIntentionName(it) }
        injectionClassField.text?.let { ErrorBuilderPluginSettings.instance.setInjectionClass(it) }
    }

    fun reset() {
        intentionNameField.text = ErrorBuilderPluginSettings.instance.getIntentionName()
        injectionClassField.text = ErrorBuilderPluginSettings.instance.getInjectionClass()
    }
}