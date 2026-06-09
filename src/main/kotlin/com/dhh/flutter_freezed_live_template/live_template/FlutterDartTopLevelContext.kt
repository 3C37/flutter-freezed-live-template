package com.dhh.flutter_freezed_live_template.live_template

import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType

class FlutterDartTopLevelContext : TemplateContextType("Dart top-level") {
    override fun isInContext(templateActionContext: TemplateActionContext): Boolean {
        val file = templateActionContext.file
        if (!file.name.endsWith(".dart", ignoreCase = true)) return false

        val text = file.viewProvider.document?.charsSequence ?: file.text ?: return false
        return DartTopLevelDetector.isTopLevel(text, templateActionContext.startOffset)
    }
}
