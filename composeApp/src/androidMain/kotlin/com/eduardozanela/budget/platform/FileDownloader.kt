package com.eduardozanela.budget.platform

/**
 * Triggers a file download in the current platform's environment.
 * This is an `expect` function, requiring platform-specific implementations.
 */
actual fun downloadFile(fileName: String, content: ByteArray, mimeType: String) {

}