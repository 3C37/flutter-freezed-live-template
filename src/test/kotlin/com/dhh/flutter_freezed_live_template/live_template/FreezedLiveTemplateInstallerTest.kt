package com.dhh.flutter_freezed_live_template.live_template

import com.dhh.flutter_freezed_live_template.nesting.FlutterFileNestingSettings
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FreezedLiveTemplateInstallerTest {
    @Test
    fun `updates stale user templates to bundled Freezed 3 syntax once`() {
        val store = RecordingLiveTemplateStore(
            "frzdmodel" to "@freezed\nclass $" + "CLASS_NAME$ with _$$" + "CLASS_NAME$ {}",
        )
        val state = FlutterFileNestingSettings.State()

        assertTrue(
            FreezedLiveTemplateInstaller.installCurrentTemplatesOnce(
                state = state,
                store = store,
                templates = listOf(
                    FreezedLiveTemplateInstaller.BundledLiveTemplate(
                        key = "frzdmodel",
                        groupName = FreezedLiveTemplateInstaller.GROUP_NAME,
                        text = "@freezed\nabstract class $" + "CLASS_NAME$ with _$$" + "CLASS_NAME$ {\n" +
                            "factory fromJson(Map<String, Object?> json);\n}",
                    ),
                ),
            ),
        )

        val updated = store.templates.getValue("frzdmodel")
        assertTrue(updated.contains("abstract class $" + "CLASS_NAME$"))
        assertTrue(updated.contains("Map<String, Object?> json"))
        assertFalse(updated.contains("\nclass $" + "CLASS_NAME$ with"))
        assertTrue(state.liveTemplatesVersion >= FreezedLiveTemplateInstaller.CURRENT_VERSION)
    }

    @Test
    fun `does not overwrite templates after migration has already run`() {
        val store = RecordingLiveTemplateStore("frzd" to "custom user template")
        val state = FlutterFileNestingSettings.State(
            liveTemplatesVersion = FreezedLiveTemplateInstaller.CURRENT_VERSION,
        )

        assertFalse(
            FreezedLiveTemplateInstaller.installCurrentTemplatesOnce(
                state = state,
                store = store,
                templates = listOf(
                    FreezedLiveTemplateInstaller.BundledLiveTemplate(
                        key = "frzd",
                        groupName = FreezedLiveTemplateInstaller.GROUP_NAME,
                        text = "bundled template",
                    ),
                ),
            ),
        )

        assertTrue(store.templates.getValue("frzd") == "custom user template")
    }

    private class RecordingLiveTemplateStore(
        vararg initialTemplates: Pair<String, String>,
    ) : FreezedLiveTemplateInstaller.LiveTemplateStore {
        val templates: MutableMap<String, String> = mutableMapOf(*initialTemplates)

        override fun upsert(template: FreezedLiveTemplateInstaller.BundledLiveTemplate) {
            templates[template.key] = template.text
        }
    }
}
