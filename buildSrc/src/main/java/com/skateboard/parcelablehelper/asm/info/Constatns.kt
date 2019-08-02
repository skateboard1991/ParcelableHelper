package com.skateboard.parcelablehelper.asm.info

class Constatns {
    
    companion object{
        val PARCELABLE_ANNOATION_NAME = "Lcom/skateboard/parcelableannoation/Parcelable;"

        val PARCELABLE_IGNORE_ANNOATION_DESCRIPTOR = "Lcom/skateboard/parcelableannoation/Ignore;"

        val PARCELABLE_CREATOR_DESCRIPTOR = "Landroid/os/Parcelable${'$'}Creator;"

        val PARCELABLE_CREATOR_INTERNALNAME = "android/os/Parcelable${'$'}Creator"

        val PARCELABLE_INTERNALNAME = "android/os/Parcelable"

        val PARCELABLE_DESCRIPTOR = "Landroid/os/Parcelable;"

        val PARCEL_INTERNALNAME="android/os/Parcel"

        val PARCEL_DESCRIPTOR="Landroid/os/Parcel;"

        var BuiltInTypeMap = mutableMapOf<String, String>()
        
        init {

            BuiltInTypeMap["B"] = "Byte"
            BuiltInTypeMap["[B"] = "ByteArray"
            BuiltInTypeMap["I"] = "Int"
            BuiltInTypeMap["[I"] = "IntArray"
            BuiltInTypeMap["J"] = "Long"
            BuiltInTypeMap["[J"] = "LongArray"
            BuiltInTypeMap["Z"] = "Boolean"
            BuiltInTypeMap["[Z"] = "BooleanArray"
            BuiltInTypeMap["F"] = "Float"
            BuiltInTypeMap["[F"] = "FloatArray"
            BuiltInTypeMap["D"] = "Double"
            BuiltInTypeMap["[D"] = "DoubleArray"
            BuiltInTypeMap["Ljava/lang/Byte;"] = "Byte"
            BuiltInTypeMap["[Ljava/lang/Byte;"] = "ByteArray"
            BuiltInTypeMap["Ljava/lang/Integer;"] = "Int"
            BuiltInTypeMap["[Ljava/lang/Integer;"] = "IntArray"
            BuiltInTypeMap["Ljava/lang/Long;"] = "Long"
            BuiltInTypeMap["[Ljava/lang/Long;"] = "LongArray"
            BuiltInTypeMap["Ljava/lang/Boolean;"] = "Boolean"
            BuiltInTypeMap["[Ljava/lang/Boolean;"] = "BooleanArray"
            BuiltInTypeMap["Ljava/lang/Float;"] = "Float"
            BuiltInTypeMap["[Ljava/lang/Float;"] = "FloatArray"
            BuiltInTypeMap["Ljava/lang/Double;"] = "Double"
            BuiltInTypeMap["[Ljava/lang/Double;"] = "DoubleArray"
            BuiltInTypeMap["Ljava/lang/String;"] = "String"
            BuiltInTypeMap["[Ljava/lang/String;"] = "StringArray"
            BuiltInTypeMap["Ljava/io/Serializable;"] = "Serializable"
            BuiltInTypeMap["Landroid/os/Parcelable"] = "Parcelable"
        }
    }
}