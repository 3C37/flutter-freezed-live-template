package com.dhh.flutter_freezed_live_template.nesting

import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project

object FlutterFileNestingApplicator {
    fun apply(project: Project): Boolean {
        val settings = ApplicationManager.getApplication().getService(FlutterFileNestingSettings::class.java)
        val projectView = ProjectView.getInstance(project)
        val controller = object : FileNestingController {
            override var isEnabled: Boolean
                get() = projectView.isUseFileNestingRules(currentOrDefaultViewId(projectView))
                set(value) = projectView.setUseFileNestingRules(value)

            override fun refresh() {
                projectView.refresh()
            }
        }

        return FileNestingInstaller.installOnce(settings.state, controller)
    }

    private fun currentOrDefaultViewId(projectView: ProjectView): String =
        projectView.currentViewId ?: projectView.defaultViewId
}
