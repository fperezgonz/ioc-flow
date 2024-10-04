package solutions.sulfura.iocflow.menu

import com.intellij.openapi.util.text.StringUtil
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import java.awt.BorderLayout
import java.util.ArrayList
import javax.swing.JPanel
import javax.swing.table.DefaultTableModel
import kotlin.math.max

class IocFlowPluginSettingsComponent {
    val panel: JPanel

    // Column names
    private var columnNames: Array<String> = arrayOf("Intention name", "Package name", "Injection class", "Field name")
    private val tableModel: DefaultTableModel = DefaultTableModel(columnNames, 0)
    private val table: JBTable = JBTable(tableModel)

    init {

        // Using ToolbarDecorator to provide a toolbar for adding/removing rows
        val decorator: ToolbarDecorator = ToolbarDecorator.createDecorator(table)
        decorator.setAddAction { _ ->
            if (tableModel.rowCount == 0 || isLastRowFilled()) {
                tableModel.addRow(arrayOf("", "", ""))
            }
        }
        decorator.setRemoveAction { _ ->
            val selectedRow = table.selectedRow
            if (selectedRow != -1) {
                tableModel.removeRow(selectedRow)
                var newSelectedRow = max(0, selectedRow - 1)
                if (table.rowCount > selectedRow) {
                    newSelectedRow = selectedRow
                }
                table.changeSelection(newSelectedRow, 0, false, false)
            }
        }
        panel = decorator.createPanel()

        // Add the table to a scroll pane for scrolling support
        val scrollPane = JBScrollPane(table)
        // Add the scroll pane to the panel
        panel.add(scrollPane, BorderLayout.CENTER)

    }

    private fun isLastRowFilled(): Boolean {
        return tableModel.getValueAt(tableModel.rowCount - 1, 0) != ""
                && tableModel.getValueAt(tableModel.rowCount - 1, 1) != ""
                && tableModel.getValueAt(tableModel.rowCount - 1, 2) != ""

    }

    fun isModified(): Boolean {

        buildStateFromTable().forEach {

            if (!IocFlowPluginSettings.instance.state.intentDefinitions.any { elem -> elem == it }) {
                return true
            }

        }

        return false

    }

    private fun buildStateFromTable(): ArrayList<IocFlowPluginSettings.IntentDefinition> {

        val newState = ArrayList<IocFlowPluginSettings.IntentDefinition>()

        for (rowVector in tableModel.dataVector) {

            if (StringUtil.isEmpty(rowVector[0] as String?)
                || StringUtil.isEmpty(rowVector[1] as String?)
                || StringUtil.isEmpty(rowVector[2] as String?)
            ) {
                continue
            }

            newState.add(
                IocFlowPluginSettings.IntentDefinition(
                    rowVector[0] as String,
                    rowVector[1] as String,
                    rowVector[2] as String,
                    (rowVector[3] ?: rowVector[0]) as String
                )
            )

        }

        return newState

    }

    fun apply() {
        IocFlowPluginSettings.instance.loadState(IocFlowPluginSettings.State(intentDefinitions = buildStateFromTable()))
    }

    fun reset() {
        for (intentDefinition in IocFlowPluginSettings.instance.state.intentDefinitions) {
            tableModel.addRow(
                arrayOf(
                    intentDefinition.intentionName,
                    intentDefinition.packageName,
                    intentDefinition.injectionClass,
                    intentDefinition.fieldName
                )
            )
        }
    }
}