package solutions.sulfura.errorbuilderplugin

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.*

public class ErrorBuilderIntention : PsiElementBaseIntentionAction(), IntentionAction {

    override fun getText(): String {
        return familyName
    }

    override fun getFamilyName(): String {
        return "Gen-d: add errorBuilder to class and configure error"
    }

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {

        return element.prevSibling.text.lowercase() == "errorbuilder"

    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {

        var parent: PsiElement? = element

        while (parent != null && parent !is PsiClass) {
            parent = parent.parent
        }

        if (parent == null || parent !is PsiClass) {
            return
        }

        val fieldExists = parent.fields.any() { it.name == "errorBuilder" }

        if (fieldExists) {
            return
        }

        val elementFactory = JavaPsiFacade.getElementFactory(project)
        val errorBuilderType = elementFactory.createTypeFromText("ErrorBuilder", parent)

        //Add field
        val newField = elementFactory.createField("errorBuilder", errorBuilderType)
        parent.add(newField);

        //Add parameter to constructors
        for (method in parent.constructors) {

            val newParam = elementFactory.createParameter("errorBuilder", errorBuilderType)
            val stmt = elementFactory.createStatementFromText("this.errorBuilder = errorBuilder;", parent)
            method.parameterList.add(newParam)
            method.body?.add(stmt)

        }

        //Add new constructor
        if (parent.constructors.isEmpty()) {

            try {

                val newConstructor: PsiMethod = JavaPsiFacade.getElementFactory(project).createMethodFromText(
                    """public ${parent.name}(ErrorBuilder errorBuilder){
                            this.errorBuilder = errorBuilder;
                        }""", parent
                )

                parent.add(newConstructor);

            } catch (e: Throwable) {
                println(e)
            }

            return

        }
    }


}