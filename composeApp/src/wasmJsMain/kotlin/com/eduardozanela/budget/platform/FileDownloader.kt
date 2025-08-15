package com.eduardozanela.budget.platform

import com.eduardozanela.budget.utils.toJsArray
import kotlinx.browser.document
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag


/**
 * The browser-specific implementation for downloading a file using a **Data URL**.
 * This is a robust alternative to the `Blob` approach that avoids complex object creation
 * and potential browser interoperability issues. It works by embedding the file
 * content directly into the link's `href` attribute.
 */
actual fun downloadFile(fileName: String, content: ByteArray, mimeType: String) {
    val uint8Array = content.toJsArray()
    val parts: JsArray<JsAny?> = listOf(uint8Array).toJsArray()

    val blob = Blob(
        parts,
        BlobPropertyBag(type = mimeType)
    )

    val dataUrl = URL.createObjectURL(blob)
    // Create an anchor element
    val anchor = document.createElement("a") as HTMLAnchorElement
    anchor.href = dataUrl
    anchor.download = fileName

    // Append it to the DOM
    document.body?.appendChild(anchor)
    // Trigger a click to start download
    anchor.click()
    // Clean up: remove anchor and revoke object URL
    document.body?.removeChild(anchor)
}
