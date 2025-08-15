package com.eduardozanela.budget.utils

import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get

private fun toJsArrayImpl(vararg x: Byte): Uint8Array = js("new Uint8Array(x)")

public fun ByteArray.toJsArray(): Uint8Array = toJsArrayImpl(*this)

public fun Uint8Array.toByteArray(): ByteArray =
    ByteArray(this.length) { this[it] }

public fun ArrayBuffer.toByteArray(): ByteArray {
    val uint8Array = Uint8Array(this) // Create a view of the ArrayBuffer
    // Create a Kotlin ByteArray by dynamically accessing the JS array's elements
    // and converting the JS `Number` to a Kotlin `Byte`.
    return ByteArray(uint8Array.length) { index -> uint8Array.get(index).toByte()  }
}