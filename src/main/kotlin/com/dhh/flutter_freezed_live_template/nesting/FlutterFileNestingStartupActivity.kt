package com.dhh.flutter_freezed_live_template.nesting

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class FlutterFileNestingStartupActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        FlutterFileNestingApplicator.apply(project)
    }
}
