package com.dhh.flutter_freezed_live_template.nesting

interface FileNestingController {
    var isEnabled: Boolean
    fun refresh()
}

object FileNestingInstaller {
    fun installOnce(
        state: FlutterFileNestingSettings.State,
        controller: FileNestingController,
    ): Boolean {
        if (state.applied) return false

        if (!controller.isEnabled) {
            controller.isEnabled = true
        }
        controller.refresh()
        state.applied = true
        return true
    }
}
