package com.skateboard.parcelablehelper.asm.info

data class FieldInfo(
    var access: Int,
    var name: String?,
    var descriptor: String?,
    var signature: String?,
    var value: Any?,
    var isIgnore:Boolean=false
)