package com.eduardozanela.budget

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable


fun main() {
    renderComposable(rootElementId = "root") {
        var count by remember { mutableStateOf(0) }

        H1 { Text("Hello from Compose for Wasm!") }

        Button(attrs = {
            onClick { count++ }
        }) {
            Text("You clicked $count times")
        }
    }
}