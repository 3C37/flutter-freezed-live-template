package com.dhh.flutter_freezed_live_template.live_template

/**
 * Lightweight Dart top-level detector that avoids depending on the Dart plugin.
 *
 * Live Template contexts are evaluated very early and must remain available even when
 * Android/Flutter/Dart plugins are absent during Marketplace verification. For this
 * plugin's use case, a brace/bracket/parenthesis scan is enough to hide the templates
 * from class/function bodies while keeping them available after imports and part files.
 */
object DartTopLevelDetector {
    fun isTopLevel(text: CharSequence, offset: Int): Boolean {
        var braces = 0
        var brackets = 0
        var parentheses = 0
        var state = State.NORMAL
        var i = 0
        val end = offset.coerceIn(0, text.length)

        while (i < end) {
            val c = text[i]
            val next = text.getOrNull(i + 1)

            when (state) {
                State.NORMAL -> when {
                    c == '/' && next == '/' -> {
                        state = State.LINE_COMMENT
                        i++
                    }
                    c == '/' && next == '*' -> {
                        state = State.BLOCK_COMMENT
                        i++
                    }
                    c == '\'' -> state = State.SINGLE_QUOTED_STRING
                    c == '"' -> state = State.DOUBLE_QUOTED_STRING
                    c == '{' -> braces++
                    c == '}' -> braces = (braces - 1).coerceAtLeast(0)
                    c == '[' -> brackets++
                    c == ']' -> brackets = (brackets - 1).coerceAtLeast(0)
                    c == '(' -> parentheses++
                    c == ')' -> parentheses = (parentheses - 1).coerceAtLeast(0)
                }
                State.LINE_COMMENT -> if (c == '\n' || c == '\r') {
                    state = State.NORMAL
                }
                State.BLOCK_COMMENT -> if (c == '*' && next == '/') {
                    state = State.NORMAL
                    i++
                }
                State.SINGLE_QUOTED_STRING -> when {
                    c == '\\' -> i++
                    c == '\'' -> state = State.NORMAL
                }
                State.DOUBLE_QUOTED_STRING -> when {
                    c == '\\' -> i++
                    c == '"' -> state = State.NORMAL
                }
            }
            i++
        }

        return braces == 0 && brackets == 0 && parentheses == 0 && state != State.BLOCK_COMMENT
    }

    private enum class State {
        NORMAL,
        LINE_COMMENT,
        BLOCK_COMMENT,
        SINGLE_QUOTED_STRING,
        DOUBLE_QUOTED_STRING,
    }
}
