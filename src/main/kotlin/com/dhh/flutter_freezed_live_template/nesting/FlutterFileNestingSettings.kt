package com.dhh.flutter_freezed_live_template.nesting

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@Service(Service.Level.APP)
@State(
    name = "FlutterFreezedFileNestingSettings",
    storages = [Storage("flutter-freezed-live-template.xml")],
)
class FlutterFileNestingSettings : PersistentStateComponent<FlutterFileNestingSettings.State> {
    private var state = State()

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
    }

    data class State(
        var applied: Boolean = false,
    )
}
