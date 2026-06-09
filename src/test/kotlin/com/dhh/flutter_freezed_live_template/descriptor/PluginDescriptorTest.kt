package com.dhh.flutter_freezed_live_template.descriptor

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory

class PluginDescriptorTest {
    @Test
    fun `live templates use plugin owned top-level Dart context`() {
        val document = readResourceXml("live_templates/flutter_freezed_live_template.xml")
        val templates = document.getElementsByTagName("template")
        val contexts = (0 until templates.length).map { index ->
            val template = templates.item(index)
            val options = template.childNodes.asSequence()
                .filter { it.nodeName == "context" }
                .flatMap { it.childNodes.asSequence() }
                .filter { it.nodeName == "option" }
                .toList()
            options.single().attributes.getNamedItem("name").nodeValue
        }

        assertEquals(listOf("FLUTTER_FREEZED_DART_TOPLEVEL", "FLUTTER_FREEZED_DART_TOPLEVEL"), contexts)
        assertFalse(contexts.contains("DART_TOPLEVEL"))
    }

    @Test
    fun `live templates use Freezed 3 primary constructor syntax`() {
        val document = readResourceXml("live_templates/flutter_freezed_live_template.xml")
        val templates = document.getElementsByTagName("template")
        val templateValues = (0 until templates.length).associate { index ->
            val template = templates.item(index)
            template.attributes.getNamedItem("name").nodeValue to template.attributes.getNamedItem("value").nodeValue
        }
        val classPlaceholder = "${'$'}CLASS_NAME${'$'}"
        val mixinPlaceholder = "_${'$'}${'$'}${'$'}CLASS_NAME${'$'}"

        templateValues.values.forEach { value ->
            assertTrue(value.contains("abstract class $classPlaceholder with $mixinPlaceholder"))
            assertTrue(value.contains("const $classPlaceholder._();"))
            assertFalse(value.contains("\nclass $classPlaceholder with"))
        }
        assertTrue(templateValues.getValue("frzdmodel").contains("Map<String, Object?> json"))
        assertFalse(templateValues.getValue("frzdmodel").contains("Map<String, dynamic> json"))
    }

    @Test
    fun `plugin descriptor avoids Android and Flutter mandatory dependencies`() {
        val document = readResourceXml("META-INF/plugin.xml")
        val dependencyNodes = document.getElementsByTagName("depends")
        val dependencies = (0 until dependencyNodes.length).map { index ->
            dependencyNodes.item(index).textContent.trim()
        }

        assertTrue(dependencies.contains("com.intellij.modules.platform"))
        assertFalse(dependencies.contains("org.jetbrains.android"))
        assertFalse(dependencies.contains("io.flutter"))
    }

    private fun readResourceXml(path: String) =
        (javaClass.classLoader.getResourceAsStream(path) ?: error("$path resource missing")).use(::parseXml)

    private fun parseXml(input: InputStream) =
        DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .parse(input)

    private fun org.w3c.dom.NodeList.asSequence(): Sequence<org.w3c.dom.Node> = sequence {
        for (index in 0 until length) yield(item(index))
    }
}
