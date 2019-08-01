package com.skateboard.parcelablehelper.asm

import com.skateboard.parcelablehelper.asm.info.ClassInfo
import com.skateboard.parcelablehelper.asm.info.Constatns.Companion.PARCELABLE_CREATOR_INTERNALNAME
import com.skateboard.parcelablehelper.asm.info.Constatns.Companion.PARCELABLE_INTERNALNAME
import com.skateboard.parcelablehelper.asm.info.Constatns.Companion.PARCEL_DESCRIPTOR
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type
import com.sun.org.apache.bcel.internal.generic.ARETURN
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL
import com.sun.org.apache.bcel.internal.generic.ILOAD
import com.sun.org.apache.bcel.internal.generic.ALOAD


class ParcelableCreatorGenerator(private val classInfo: ClassInfo, private val cv: ClassWriter) {

    val internalName = classInfo.name + "${'$'}1"

    val superClassDescriptor = Type.getObjectType(classInfo.superName).descriptor

    val classDescriptor = Type.getObjectType(classInfo.name).descriptor

    fun dump(): ByteArray {
        generateClass()
        generateInnerClass()
        generateInitMethod()
        genearteCreateFromParcelMethod()
        generateNewArrayMethod()
        cv.visitEnd()
        return cv.toByteArray()
    }

    private fun generateClass() {
        cv.visit(
            V1_7,
            ACC_STATIC + ACC_FINAL,
            internalName,
            "Ljava/lang/Object;Landroid/os/Parcelable${'$'}Creator<$superClassDescriptor>;",
            "java/lang/Object",
            arrayOf<String>(PARCELABLE_CREATOR_INTERNALNAME)
        )
    }

    private fun generateInnerClass() {
        cv.visitInnerClass(
            PARCELABLE_CREATOR_INTERNALNAME,
            PARCELABLE_INTERNALNAME,
            "Creator",
            ACC_PUBLIC + ACC_STATIC + ACC_ABSTRACT + ACC_INTERFACE
        )
    }

    private fun generateInitMethod() {
        val mv = cv.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null)
        mv.visitCode()
        mv.visitVarInsn(ALOAD, 0)
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
        mv.visitInsn(RETURN)
        mv.visitEnd()
    }

    private fun genearteCreateFromParcelMethod() {
        var mv = cv.visitMethod(
            ACC_PUBLIC,
            "createFromParcel",
            "($PARCEL_DESCRIPTOR)$classDescriptor",
            null,
            null
        )
        mv.visitCode()
        mv.visitTypeInsn(NEW, classInfo.name)
        mv.visitInsn(DUP)
        mv.visitVarInsn(ALOAD, 1)
        mv.visitMethodInsn(INVOKESPECIAL, classInfo.name, "<init>", "($PARCEL_DESCRIPTOR)V", false)
        mv.visitInsn(ARETURN)
        mv.visitEnd()

        mv = cv.visitMethod(
            ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC,
            "createFromParcel",
            "($PARCEL_DESCRIPTOR)$superClassDescriptor",
            null,
            null
        )
        mv.visitCode()
        mv.visitVarInsn(ALOAD, 0)
        mv.visitVarInsn(ALOAD, 1)
        mv.visitMethodInsn(
            INVOKEVIRTUAL,
            internalName,
            "createFromParcel",
            "($PARCEL_DESCRIPTOR)$classDescriptor",
            false
        )
        mv.visitInsn(ARETURN)
        mv.visitEnd()
    }

    private fun generateNewArrayMethod() {
        var mv = cv.visitMethod(
            ACC_PUBLIC,
            "newArray",
            "(I)[$classDescriptor",
            null,
            null
        )
        mv.visitCode()
        mv.visitVarInsn(ILOAD, 1)
        mv.visitTypeInsn(ANEWARRAY, classInfo.name)
        mv.visitInsn(ARETURN)
        mv.visitEnd()




        mv =
            cv.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "newArray", "(I)[$superClassDescriptor", null, null)
        mv.visitCode()
        mv.visitVarInsn(ALOAD, 0)
        mv.visitVarInsn(ILOAD, 1)
        mv.visitMethodInsn(
            INVOKEVIRTUAL,
            internalName,
            "newArray",
            "(I)[$classDescriptor",
            false
        )
        mv.visitInsn(ARETURN)
        mv.visitEnd()
    }
}