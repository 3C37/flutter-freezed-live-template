package com.dhh.flutter_freezed_live_template.live_template

import com.dhh.flutter_freezed_live_template.nesting.FlutterFileNestingSettings
import com.intellij.codeInsight.template.impl.TemplateImpl
import com.intellij.codeInsight.template.impl.TemplateSettings
import org.jdom.input.SAXBuilder

object FreezedLiveTemplateInstaller {
    const val GROUP_NAME: String = "Flutter freezed snippets"
    const val CURRENT_VERSION: Int = 1

    fun installCurrentTemplatesOnce(
        state: FlutterFileNestingSettings.State,
        templateSettings: TemplateSettings = TemplateSettings.getInstance(),
    ): Boolean = installCurrentTemplatesOnce(
        state = state,
        store = TemplateSettingsLiveTemplateStore(templateSettings),
        templates = readBundledTemplates(),
    )

    internal fun installCurrentTemplatesOnce(
        state: FlutterFileNestingSettings.State,
        store: LiveTemplateStore,
        templates: List<BundledLiveTemplate> = readBundledTemplates(),
    ): Boolean {
        if (state.liveTemplatesVersion >= CURRENT_VERSION) return false

        templates.forEach(store::upsert)
        state.liveTemplatesVersion = CURRENT_VERSION
        return true
    }

    internal fun readBundledTemplates(): List<BundledLiveTemplate> {
        val classLoader = FreezedLiveTemplateInstaller::class.java.classLoader
        val stream = classLoader.getResourceAsStream("live_templates/flutter_freezed_live_template.xml")
            ?: error("Bundled Freezed live template resource is missing")

        return stream.use { input ->
            SAXBuilder().build(input).rootElement.getChildren("template").map { element ->
                val template = TemplateSettings.readTemplateFromElement(GROUP_NAME, element, classLoader)
                BundledLiveTemplate(
                    key = template.key,
                    groupName = template.groupName,
                    text = template.string,
                    template = template,
                )
            }
        }
    }

    internal data class BundledLiveTemplate(
        val key: String,
        val groupName: String,
        val text: String,
        val template: TemplateImpl? = null,
    )

    internal interface LiveTemplateStore {
        fun upsert(template: BundledLiveTemplate)
    }

    private class TemplateSettingsLiveTemplateStore(
        private val templateSettings: TemplateSettings,
    ) : LiveTemplateStore {
        override fun upsert(template: BundledLiveTemplate) {
            val desiredTemplate = template.template ?: TemplateImpl(template.key, template.text, template.groupName)
            val existingTemplate = templateSettings.getTemplate(template.key, template.groupName)
            if (existingTemplate != null) {
                existingTemplate.resetFrom(desiredTemplate)
            } else {
                templateSettings.addTemplate(desiredTemplate)
            }
        }
    }
}
