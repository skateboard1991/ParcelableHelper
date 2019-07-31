package com.skateboard.parcelablehelper.asm

import com.skateboard.parcelablehelper.asm.info.ClassInfo
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type


class ParcelableCreatorGenerator(private val classInfo: ClassInfo, private val cv: ClassWriter) {


    fun dump(): ByteArray {

        val internalName = classInfo.name + "${'$'}1"

        val superClassDescriptor = Type.getObjectType(classInfo.name).descriptor

        cv.visit(
            V1_7,
            ACC_STATIC + ACC_FINAL,
            internalName,
            "Ljava/lang/Object;Landroid/os/Parcelable${'$'}Creator<${superClassDescriptor}>;",
            "java/lang/Object",
            arrayOf<String>("android/os/Parcelable${'$'}Creator")
        )

        cv.visitInnerClass(
            "android/os/Parcelable${'$'}Creator",
            "android/os/Parcelable",
            "Creator",
            ACC_PUBLIC + ACC_STATIC + ACC_ABSTRACT + ACC_INTERFACE
        )

        run {
            val mv = cv.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null)
            mv.visitCode()
            mv.visitVarInsn(ALOAD, 0)
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
            mv.visitInsn(RETURN)
            mv.visitEnd()
        }
        run {
            val mv = cv.visitMethod(
                ACC_PUBLIC,
                "createFromParcel",
                "(Landroid/os/Parcel;)$superClassDescriptor",
                null,
                null
            )
            mv.visitCode()
            mv.visitTypeInsn(NEW, classInfo.name)
            mv.visitInsn(DUP)
            mv.visitVarInsn(ALOAD, 1)
            mv.visitMethodInsn(INVOKESPECIAL, classInfo.name, "<init>", "(Landroid/os/Parcel;)V", false)
            mv.visitInsn(ARETURN)
            mv.visitEnd()
        }
        run {
            val mv = cv.visitMethod(
                ACC_PUBLIC,
                "newArray",
                "(I)[$superClassDescriptor",
                null,
                null
            )
            mv.visitCode()
            mv.visitVarInsn(ILOAD, 1)
            mv.visitTypeInsn(ANEWARRAY, classInfo.name)
            mv.visitInsn(ARETURN)
            mv.visitEnd()
        }
        cv.visitEnd()
        return cv.toByteArray()
    }
}