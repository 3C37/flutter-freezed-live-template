package com.dhh.flutter_freezed_live_template.nesting

import com.intellij.ide.projectView.ProjectViewNestingRulesProvider
import org.junit.Assert.assertEquals
import org.junit.Test

class FlutterNestingRulesProviderTest {
    @Test
    fun `registers freezed and json generated dart files under dart parents`() {
        val rules = mutableListOf<Pair<String, String>>()
        val consumer = ProjectViewNestingRulesProvider.Consumer { parent, child -> rules += parent to child }

        FlutterNestingRulesProvider().addFileNestingRules(consumer)

        assertEquals(listOf(".dart" to ".freezed.dart", ".dart" to ".g.dart"), rules)
    }
}
