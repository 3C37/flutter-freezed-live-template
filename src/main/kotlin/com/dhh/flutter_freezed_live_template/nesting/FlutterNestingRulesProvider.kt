package com.dhh.flutter_freezed_live_template.nesting

import com.intellij.ide.projectView.ProjectViewNestingRulesProvider
import com.intellij.ide.projectView.ProjectViewNestingRulesProvider.Consumer
import org.jetbrains.annotations.NotNull

class FlutterNestingRulesProvider : ProjectViewNestingRulesProvider {
    override fun addFileNestingRules(@NotNull consumer: Consumer) {
        consumer.addNestingRule(".dart", ".freezed.dart")
        consumer.addNestingRule(".dart", ".g.dart")
    }
}