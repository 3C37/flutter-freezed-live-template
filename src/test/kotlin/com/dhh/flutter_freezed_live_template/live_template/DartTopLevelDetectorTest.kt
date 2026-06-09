package com.dhh.flutter_freezed_live_template.live_template

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DartTopLevelDetectorTest {
    @Test
    fun `accepts insertion after imports and part directives`() {
        val text = """
            import 'package:freezed_annotation/freezed_annotation.dart';
            
            part 'user.freezed.dart';
            part 'user.g.dart';
            
            frzd
        """.trimIndent()

        assertTrue(DartTopLevelDetector.isTopLevel(text, text.length))
    }

    @Test
    fun `rejects insertion inside a class body`() {
        val text = """
            class Existing {
              frzd
            }
        """.trimIndent()
        val offset = text.indexOf("frzd") + "frzd".length

        assertFalse(DartTopLevelDetector.isTopLevel(text, offset))
    }

    @Test
    fun `ignores braces in strings and comments while checking nesting`() {
        val text = """
            const value = '{ not a block }';
            // { not a block
            /* { still not a block } */
            frzdmodel
        """.trimIndent()

        assertTrue(DartTopLevelDetector.isTopLevel(text, text.length))
    }
}
