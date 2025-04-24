package com.dhh.flutter_freezed_live_template.nesting

import com.intellij.ide.projectView.ProjectViewNestingRulesProvider

class FlutterNestingRulesProvider : ProjectViewNestingRulesProvider {

    override fun addFileNestingRules(consumer: ProjectViewNestingRulesProvider.Consumer) {
        consumer.addNestingRule(".dart", ".freezed.dart")
        consumer.addNestingRule(".dart", ".g.dart")
    }
}