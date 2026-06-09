package com.dhh.flutter_freezed_live_template.live_template

import com.dhh.flutter_freezed_live_template.nesting.FlutterFileNestingSettings
import com.intellij.codeInsight.template.impl.TemplateImpl
import com.intellij.codeInsight.template.impl.TemplateSettings
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory

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
        val stream = FreezedLiveTemplateInstaller::class.java.classLoader
            .getResourceAsStream("live_templates/flutter_freezed_live_template.xml")
            ?: error("Bundled Freezed live template resource is missing")

        return stream.use(::readBundledTemplates)
    }

    internal fun readBundledTemplates(input: InputStream): List<BundledLiveTemplate> {
        val document = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .parse(input)
        val nodes = document.getElementsByTagName("template")
        return (0 until nodes.length).map { index ->
            val node = nodes.item(index)
            val attributes = node.attributes
            BundledLiveTemplate(
                key = attributes.getNamedItem("name").nodeValue,
                groupName = GROUP_NAME,
                text = attributes.getNamedItem("value").nodeValue,
                description = attributes.getNamedItem("description")?.nodeValue ?: "",
                toReformat = attributes.getNamedItem("toReformat")?.nodeValue.toBoolean(),
                toShortenLongNames = attributes.getNamedItem("toShortenFQNames")?.nodeValue.toBoolean(),
                variables = node.childNodes.asSequence()
                    .filter { it.nodeName == "variable" }
                    .map { variableNode ->
                        val variableAttributes = variableNode.attributes
                        VariableDefinition(
                            name = variableAttributes.getNamedItem("name").nodeValue,
                            expression = variableAttributes.getNamedItem("expression")?.nodeValue ?: "",
                            defaultValue = variableAttributes.getNamedItem("defaultValue")?.nodeValue ?: "",
                            alwaysStopAt = variableAttributes.getNamedItem("alwaysStopAt")?.nodeValue?.toBooleanStrictOrNull() ?: true,
                        )
                    }
                    .toList(),
            )
        }
    }

    internal data class BundledLiveTemplate(
        val key: String,
        val groupName: String,
        val text: String,
        val description: String = "",
        val toReformat: Boolean = false,
        val toShortenLongNames: Boolean = true,
        val variables: List<VariableDefinition> = emptyList(),
    )

    internal data class VariableDefinition(
        val name: String,
        val expression: String,
        val defaultValue: String,
        val alwaysStopAt: Boolean,
    )

    internal interface LiveTemplateStore {
        fun upsert(template: BundledLiveTemplate)
    }

    private class TemplateSettingsLiveTemplateStore(
        private val templateSettings: TemplateSettings,
    ) : LiveTemplateStore {
        override fun upsert(template: BundledLiveTemplate) {
            val replacementTemplate = template.toTemplateImpl()
            val existingTemplate = templateSettings.getTemplate(template.key, template.groupName)
            if (existingTemplate != null) {
                preserveExistingContext(replacementTemplate, existingTemplate)
                existingTemplate.resetFrom(replacementTemplate)
            } else {
                templateSettings.addTemplate(replacementTemplate)
            }
        }

        private fun preserveExistingContext(
            replacementTemplate: TemplateImpl,
            existingTemplate: TemplateImpl,
        ) {
            replacementTemplate.applyContext(existingTemplate.templateContext.createCopy())
        }
    }

    private fun BundledLiveTemplate.toTemplateImpl(): TemplateImpl =
        TemplateImpl(key, text, groupName).also { template ->
            template.description = description
            template.isToReformat = toReformat
            template.isToShortenLongNames = toShortenLongNames
            variables.forEach { variable ->
                template.addVariable(
                    variable.name,
                    variable.expression,
                    variable.defaultValue,
                    variable.alwaysStopAt,
                )
            }
        }

    private fun org.w3c.dom.NodeList.asSequence(): Sequence<org.w3c.dom.Node> = sequence {
        for (index in 0 until length) yield(item(index))
    }
}
