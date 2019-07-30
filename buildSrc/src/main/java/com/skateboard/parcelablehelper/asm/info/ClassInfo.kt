package com.skateboard.parcelablehelper.asm.info

data class ClassInfo(
    var version: Int,
    var access: Int,
    var name: String?,
    var signature: String?,
    var superName: String?,
    var interfaces: Array<out String>?
)