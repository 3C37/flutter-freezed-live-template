package com.dhh.flutter_freezed_live_template.nesting

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FileNestingInstallerTest {
    @Test
    fun `enables file nesting once when it is disabled`() {
        val state = FlutterFileNestingSettings.State()
        val controller = RecordingFileNestingController(isEnabled = false)

        assertTrue(FileNestingInstaller.installOnce(state, controller))
        assertTrue(controller.isEnabled)
        assertTrue(state.applied)
        assertTrue(controller.refreshed)
    }

    @Test
    fun `does not fight a later user choice after first application`() {
        val state = FlutterFileNestingSettings.State(applied = true)
        val controller = RecordingFileNestingController(isEnabled = false)

        assertFalse(FileNestingInstaller.installOnce(state, controller))
        assertFalse(controller.isEnabled)
        assertFalse(controller.refreshed)
    }

    private class RecordingFileNestingController(
        override var isEnabled: Boolean,
    ) : FileNestingController {
        var refreshed = false

        override fun refresh() {
            refreshed = true
        }
    }
}
