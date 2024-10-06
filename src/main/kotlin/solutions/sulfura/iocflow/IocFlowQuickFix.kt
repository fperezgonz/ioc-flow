package solutions.sulfura.iocflow

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiMethod
import solutions.sulfura.iocflow.settings.IocFlowPluginSettings

class IocFlowQuickFix : LocalQuickFix {

    override fun getName(): String {
        return familyName
    }

    override fun getFamilyName(): String {
        return "Inject new field"
    }

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {

        var parent: PsiElement? = descriptor.psiElement


        //Find PsiClass

        while (parent != null && parent !is PsiClass) {
            parent = parent.parent
        }

        if (parent == null) {
            return
        }

        val classElement = parent as PsiClass

        //Find PsiJavaFile

        while (parent != null && parent !is PsiJavaFile) {
            parent = parent.parent
        }

        if (parent == null) {
            return
        }

        val fileElement = parent as PsiJavaFile

        //Find intent definition

        var intentDefinition: IocFlowPluginSettings.IntentDefinition? = null

        for (it in IocFlowPluginSettings.instance.state.intentDefinitions) {
            if (descriptor.psiElement.text.lowercase() == it.intentionName.lowercase()) {
                intentDefinition = it
            }
        }

        if (intentDefinition == null) {
            return
        }

        //Check if the import is in the file's imports list and add it if it is not

        val injectionClassFqdn = intentDefinition.packageName + "." + intentDefinition.injectionClass

        val hasImport = fileElement.importList?.allImportStatements?.any { elem ->
            elem.importReference?.qualifiedName == injectionClassFqdn
                    || elem.importReference?.qualifiedName == intentDefinition.packageName + ".*"
        }

        val elementFactory = JavaPsiFacade.getElementFactory(project)
        val intentInjectionType = elementFactory.createTypeFromText(injectionClassFqdn, classElement)

        if (hasImport == false) {

            try {

                val intentInjectionClass =
                    elementFactory.createTypeByFQClassName(injectionClassFqdn).resolve() as PsiClass
                fileElement.importList?.add(elementFactory.createImportStatement(intentInjectionClass))

            } catch (_: Throwable) {
                fileElement.importList?.add(elementFactory.createImportStatementOnDemand(intentDefinition.packageName))
            }

        }

        //Check if the field has already been injected and do nothing if it has

        val fieldName = intentDefinition.fieldName ?: intentDefinition.intentionName

        val fieldExists = classElement.fields.any { it.name == fieldName }

        if (fieldExists) {
            return
        }

        val newField = elementFactory.createField(fieldName, intentInjectionType)
        classElement.add(newField)

        //Add parameter to existing constructors
        for (method in classElement.constructors) {

            val newParam = elementFactory.createParameter(fieldName, intentInjectionType)
            val stmt = elementFactory.createStatementFromText("this.$fieldName = $fieldName;", classElement)
            method.parameterList.add(newParam)
            method.body?.add(stmt)

        }

        //Add new constructor if there are none
        if (classElement.constructors.isEmpty()) {

            try {
                val newConstructor: PsiMethod = JavaPsiFacade.getElementFactory(project).createMethodFromText(
                    """public ${classElement.name}(${intentDefinition.injectionClass} $fileElement){
                            this.$fieldName = $fieldName;
                        }""", classElement
                )

                classElement.add(newConstructor)

            } catch (e: Throwable) {
                println(e)
            }

            return

        }
    }


}