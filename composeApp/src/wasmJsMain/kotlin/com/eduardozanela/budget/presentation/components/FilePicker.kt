package com.eduardozanela.budget.presentation.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.khronos.webgl.ArrayBuffer
import org.w3c.dom.HTMLInputElement
import org.w3c.files.FileReader
import kotlinx.browser.document
import com.eduardozanela.budget.utils.toByteArray

@Composable
actual fun FilePicker(onFileSelected: (ByteArray, String) -> Unit, modifier: Modifier) {
    Button(
        onClick = {
            val input = document.createElement("input") as HTMLInputElement
            input.type = "file"
            input.accept = ".pdf" // Match the button text and expected file type

            input.onchange = { event ->
                val file = (event.target as HTMLInputElement).files?.item(0)
                file?.let { currentFile ->
                    val reader = FileReader()
                    reader.onload = { loadEvent ->
                        val content = (loadEvent.target as FileReader).result as? ArrayBuffer
                        content?.let {
                            onFileSelected(content.toByteArray(), currentFile.name)
                        }
                    }
                    reader.readAsArrayBuffer(currentFile)
                }
            }
            input.click()
        },
        modifier = modifier
    ) {
        Text("Upload Statement")
    }
}

