package com.dhh.flutter_freezed_live_template.descriptor

import org.jdom.input.SAXBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PluginDescriptorTest {
    @Test
    fun `live templates use plugin owned top-level Dart context`() {
        val stream = javaClass.classLoader.getResourceAsStream("live_templates/flutter_freezed_live_template.xml")
            ?: error("live template resource missing")
        val root = SAXBuilder().build(stream).rootElement
        val contexts = root.getChildren("template").map { template ->
            template.getChild("context").getChildren("option").single().getAttributeValue("name")
        }

        assertEquals(listOf("FLUTTER_FREEZED_DART_TOPLEVEL", "FLUTTER_FREEZED_DART_TOPLEVEL"), contexts)
        assertFalse(contexts.contains("DART_TOPLEVEL"))
    }

    @Test
    fun `live templates use Freezed 3 primary constructor syntax`() {
        val stream = javaClass.classLoader.getResourceAsStream("live_templates/flutter_freezed_live_template.xml")
            ?: error("live template resource missing")
        val root = SAXBuilder().build(stream).rootElement
        val templateValues = root.getChildren("template").associate { template ->
            template.getAttributeValue("name") to template.getAttributeValue("value")
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
        val stream = javaClass.classLoader.getResourceAsStream("META-INF/plugin.xml")
            ?: error("plugin descriptor missing")
        val root = SAXBuilder().build(stream).rootElement
        val dependencies = root.getChildren("depends").map { it.textTrim }

        assertTrue(dependencies.contains("com.intellij.modules.platform"))
        assertFalse(dependencies.contains("org.jetbrains.android"))
        assertFalse(dependencies.contains("io.flutter"))
    }
}
