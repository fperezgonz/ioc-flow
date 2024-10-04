package solutions.sulfura.iocflow

import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiErrorElement
import solutions.sulfura.iocflow.menu.IocFlowPluginSettings

class IocFlowInspection : AbstractBaseJavaLocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : JavaElementVisitor() {
            override fun visitErrorElement(element: PsiErrorElement) {
                super.visitErrorElement(element)

                for (it in IocFlowPluginSettings.instance.state.intentDefinitions) {
                    if (element.prevSibling.text.lowercase() == it.intentionName.lowercase()) {

                        holder.registerProblem(
                            element.prevSibling,
                            "IoC Flow usage needs to be configured",
                            IocFlowQuickFix()
                        )

                    }
                }

            }
        }
    }
}