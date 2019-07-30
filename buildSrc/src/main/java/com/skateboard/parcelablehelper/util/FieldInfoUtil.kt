package com.skateboard.parcelablehelper.util

import com.skateboard.parcelablehelper.asm.info.FieldInfo

object FieldInfoUtil {

    fun isArray(fieldInfo: FieldInfo): Boolean {

        return fieldInfo.descriptor?.startsWith("[") == true
    }

    fun isList(fieldInfo: FieldInfo): Boolean {
        return fieldInfo.descriptor?.startsWith("Ljava/util/List;") ?: false || fieldInfo.descriptor?.startsWith("Ljava/util/ArrayList;") ?: false
    }
}