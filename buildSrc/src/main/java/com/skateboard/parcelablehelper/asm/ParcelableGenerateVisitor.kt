package com.skateboard.parcelablehelper.asm

import com.skateboard.parcelablehelper.asm.info.Constatns.Companion.BuiltInTypeMap
import com.skateboard.parcelablehelper.asm.info.Constatns.Companion.PARCELABLE_ANNOATION_NAME
import com.skateboard.parcelablehelper.asm.info.Constatns.Companion.PARCELABLE_CREATOR_DESCRIPTOR
import com.skateboard.parcelablehelper.asm.info.Constatns.Companion.PARCELABLE_CREATOR_INTERNALNAME
import com.skateboard.parcelablehelper.asm.info.Constatns.Companion.PARCELABLE_DESCRIPTOR
import com.skateboard.parcelablehelper.asm.info.Constatns.Companion.PARCELABLE_IGNORE_ANNOATION_DESCRIPTOR
import com.skateboard.parcelablehelper.asm.info.Constatns.Companion.PARCELABLE_INTERNALNAME
import com.skateboard.parcelablehelper.asm.info.ClassInfo
import com.skateboard.parcelablehelper.asm.info.FieldInfo
import com.skateboard.parcelablehelper.util.FieldInfoUtil
import com.skateboard.parcelablehelper.util.FileUtil
import org.objectweb.asm.*
import java.io.File
import java.util.*


class ParcelableGenerateVisitor(val classFile: File, classWriter: ClassWriter) :
    ClassVisitor(Opcodes.ASM7, classWriter) {

    private var isIntrested = false

    private lateinit var classInfo: ClassInfo

    private var fieldList = mutableListOf<FieldInfo>()

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        classInfo = ClassInfo(version, access, name, signature, superName, interfaces)
        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitAnnotation(desc: String?, visible: Boolean): AnnotationVisitor? {
        desc?.let {
            if (PARCELABLE_ANNOATION_NAME == it) {
                isIntrested = true
            }
        }
        return super.visitAnnotation(desc, visible)
    }

    override fun visitField(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        value: Any?
    ): FieldVisitor {
        if (isIntrested) {
            fieldList.add(FieldInfo(access, name, descriptor, signature, value))
        }
        return ParcelableFieldVisitor(super.visitField(access, name, descriptor, signature, value))
    }

    private inner class ParcelableFieldVisitor(fieldVisitor: FieldVisitor) : FieldVisitor(Opcodes.ASM7, fieldVisitor) {

        override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
            if (PARCELABLE_IGNORE_ANNOATION_DESCRIPTOR == descriptor) {
                fieldList[fieldList.size - 1].isIgnore = true
            }
            return super.visitAnnotation(descriptor, visible)
        }
    }


    override fun visitEnd() {

        if (isIntrested) {
            generateParcelableImplemation()
            generateDescribeContent()
            generateWriteToParcel()
            generateParcelConstructor()
            generateCreatorInnerClass()
            generateCreatorMember()
        }
    }

    private fun generateCreatorInnerClass() {
        val innerClassFile = File(classFile.parentFile, classFile.name.split(".")[0] + "${'$'}1.class")
        if (innerClassFile.exists()) {
            innerClassFile.delete()
        }
        innerClassFile.createNewFile()
        val creatorGenerator = ParcelableCreatorGenerator(classInfo, ClassWriter(ClassWriter.COMPUTE_MAXS))
        FileUtil.writeToFile(innerClassFile, creatorGenerator.dump())
    }

    private fun generateParcelableImplemation() {

        val newInterfaces = Arrays.copyOf(classInfo.interfaces, (classInfo.interfaces?.size ?: 0) + 1)
        newInterfaces[newInterfaces.size - 1] = PARCELABLE_INTERNALNAME
        super.visit(
            classInfo.version,
            classInfo.access,
            classInfo.name,
            classInfo.signature,
            classInfo.superName,
            newInterfaces
        )
    }

    private fun generateCreatorMember() {

        cv.visitInnerClass("${classInfo.name}${'$'}1", null, null, Opcodes.ACC_STATIC)
        cv.visitInnerClass(
            PARCELABLE_CREATOR_INTERNALNAME,
            PARCELABLE_INTERNALNAME,
            "Creator",
            Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE
        )

        cv.visitField(
            Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC,
            "CREATOR",
            PARCELABLE_CREATOR_DESCRIPTOR,
            "Landroid/os/Parcelable${'$'}Creator<${Type.getObjectType(classInfo.name).descriptor}>;",
            null
        )
        val mv = cv.visitMethod(Opcodes.ACC_STATIC, "<clinit>", "()V", null, null)
        mv.visitCode()
        mv.visitTypeInsn(Opcodes.NEW, "${classInfo.name}${'$'}1")
        mv.visitInsn(Opcodes.DUP)
        mv.visitMethodInsn(
            Opcodes.INVOKESPECIAL,
            "${classInfo.name}${'$'}1",
            "<init>",
            "()V",
            false
        )
        mv.visitFieldInsn(
            Opcodes.PUTSTATIC,
            classInfo.name,
            "CREATOR",
            PARCELABLE_CREATOR_DESCRIPTOR
        )
        mv.visitInsn(Opcodes.RETURN)
        mv.visitEnd()
    }


    private fun generateDescribeContent() {

        val mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "describeContents", "()I", null, null)
        mv.visitCode()
        mv.visitInsn(Opcodes.ICONST_0)
        mv.visitInsn(Opcodes.IRETURN)
        mv.visitMaxs(1, 1)
        mv.visitEnd()
    }

    private fun generateWriteToParcel() {

        val mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "writeToParcel", "(Landroid/os/Parcel;I)V", null, null)
        mv.visitCode()
        if ("java/lang/Object" != classInfo.superName) {
            callSuperWriteToParcel(mv)
        }
        fieldList.forEach {
            if (it.isIgnore) {
                return@forEach
            }
            mv.visitVarInsn(Opcodes.ALOAD, 1)
            mv.visitVarInsn(Opcodes.ALOAD, 0)
            generateWriteToParcelStatement(mv, it)
        }
        mv.visitInsn(Opcodes.RETURN)
        mv.visitEnd()
    }

    private fun callSuperWriteToParcel(mv: MethodVisitor) {
        mv.visitVarInsn(Opcodes.ALOAD, 0)
        mv.visitVarInsn(Opcodes.ALOAD, 1)
        mv.visitVarInsn(Opcodes.ILOAD, 2)
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, classInfo.superName, "writeToParcel", "(Landroid/os/Parcel;I)V", true)
    }

    private fun generateWriteToParcelStatement(mv: MethodVisitor, fieldInfo: FieldInfo) {
        when {
            FieldInfoUtil.isArray(fieldInfo) -> {
                generateWriteToParcelArrayField(fieldInfo, mv)
            }
            FieldInfoUtil.isList(fieldInfo) -> {
                generateWriteToParcelListField(fieldInfo, mv)
            }
            else -> {
                generateWriteToParcelObjectField(fieldInfo, mv)
            }

        }
    }

    private fun generateWriteToParcelArrayField(fieldInfo: FieldInfo, mv: MethodVisitor) {

        if (BuiltInTypeMap.containsKey(fieldInfo.descriptor)) {
            mv.visitFieldInsn(Opcodes.GETFIELD, classInfo.name, fieldInfo.name, fieldInfo.descriptor)
            mv.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                "android/os/Parcel",
                "write${BuiltInTypeMap[fieldInfo.descriptor]}",
                "(${fieldInfo.descriptor})V",
                false
            )
        } else {
            mv.visitFieldInsn(
                Opcodes.GETFIELD,
                classInfo.name,
                fieldInfo.name,
                fieldInfo.descriptor
            )
            mv.visitVarInsn(Opcodes.ILOAD, 2)
            mv.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                "android/os/Parcel",
                "writeTypedArray",
                "([${PARCELABLE_DESCRIPTOR}I)V",
                false
            )
        }

    }


    private fun generateWriteToParcelListField(fieldInfo: FieldInfo, mv: MethodVisitor) {

        fieldInfo.signature?.let {
            val typedDescriptor = it.substring(it.indexOf("<") + 1, it.indexOf(">"))
//            println("typeparamer is $typedDescriptor")
            if (BuiltInTypeMap.containsKey(typedDescriptor)) {
                mv.visitFieldInsn(
                    Opcodes.GETFIELD,
                    classInfo.name,
                    fieldInfo.name,
                    "Ljava/util/List;"
                )
                mv.visitMethodInsn(
                    Opcodes.INVOKEVIRTUAL,
                    "android/os/Parcel",
                    "writeList",
                    "(Ljava/util/List;)V",
                    false
                )

            } else {
                mv.visitFieldInsn(
                    Opcodes.GETFIELD,
                    classInfo.name,
                    fieldInfo.name,
                    "Ljava/util/List;"
                )
                mv.visitMethodInsn(
                    Opcodes.INVOKEVIRTUAL,
                    "android/os/Parcel",
                    "writeTypedList",
                    "(Ljava/util/List;)V",
                    false
                )
            }
        }
    }

    private fun generateWriteToParcelObjectField(fieldInfo: FieldInfo, mv: MethodVisitor) {

        if (BuiltInTypeMap.containsKey(fieldInfo.descriptor)) {

            mv.visitFieldInsn(
                Opcodes.GETFIELD,
                classInfo.name,
                fieldInfo.name,
                fieldInfo.descriptor
            )
            mv.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                "android/os/Parcel",
                "write${BuiltInTypeMap[fieldInfo.descriptor]}",
                "(${fieldInfo.descriptor})V",
                false
            )
        } else {
            mv.visitFieldInsn(
                Opcodes.GETFIELD,
                classInfo.name,
                fieldInfo.name,
                fieldInfo.descriptor
            )
            mv.visitVarInsn(Opcodes.ILOAD, 2)
            mv.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                "android/os/Parcel",
                "writeParcelable",
                "(${PARCELABLE_DESCRIPTOR}I)V",
                false
            )
        }
    }


    private fun generateParcelConstructor() {
        val mv = cv.visitMethod(Opcodes.ACC_PROTECTED, "<init>", "(Landroid/os/Parcel;)V", null, null)
        mv.visitCode()
        callSuperParcelConstructor(mv)
        fieldList.forEach {
            if (it.isIgnore) {
                return@forEach
            }
            mv.visitVarInsn(Opcodes.ALOAD, 0)
            mv.visitVarInsn(Opcodes.ALOAD, 1)
            generateParcelConstructorStatement(mv, it)
        }
        mv.visitInsn(Opcodes.RETURN)
        mv.visitEnd()
    }

    private fun callSuperParcelConstructor(mv: MethodVisitor) {
        if ("java/lang/Object" != classInfo.superName) {
            mv.visitVarInsn(Opcodes.ALOAD, 0)
            mv.visitVarInsn(Opcodes.ALOAD, 1)
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, classInfo.superName, "<init>", "(Landroid/os/Parcel;)V", false)
        } else {
            mv.visitVarInsn(Opcodes.ALOAD, 0)
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, classInfo.superName, "<init>", "()V", false)
        }
    }

    private fun generateParcelConstructorStatement(mv: MethodVisitor, fieldInfo: FieldInfo) {
        when {
            FieldInfoUtil.isArray(fieldInfo) -> {
                generateParcelConstructorArrayField(fieldInfo, mv)
            }
            FieldInfoUtil.isList(fieldInfo) -> {
                generateParcelConstructorListField(fieldInfo, mv)
            }
            else -> {
                generateParcelConstructorObjectField(fieldInfo, mv)
            }

        }
    }

    private fun generateParcelConstructorArrayField(fieldInfo: FieldInfo, mv: MethodVisitor) {
        if (BuiltInTypeMap.containsKey(fieldInfo.descriptor)) {

            mv.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                "android/os/Parcel",
                "create${BuiltInTypeMap[fieldInfo.descriptor]}",
                "()${fieldInfo.descriptor}",
                false
            )
            mv.visitFieldInsn(Opcodes.PUTFIELD, classInfo.name, fieldInfo.name, fieldInfo.descriptor)
        } else {
            mv.visitFieldInsn(
                Opcodes.GETSTATIC,
                Type.getType(fieldInfo.descriptor?.substring(1)).internalName,
                "CREATOR",
                PARCELABLE_CREATOR_DESCRIPTOR
            )
            mv.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                "android/os/Parcel",
                "createTypedArray",
                "($PARCELABLE_CREATOR_DESCRIPTOR)[Ljava/lang/Object;",
                false
            )
//            mv.visitTypeInsn(Opcodes.CHECKCAST, fieldInfo.descriptor)
            mv.visitTypeInsn(Opcodes.CHECKCAST, Type.getType(fieldInfo.descriptor).internalName)
            mv.visitFieldInsn(Opcodes.PUTFIELD, classInfo.name, fieldInfo.name, "${fieldInfo.descriptor}")
        }
    }

    private fun generateParcelConstructorListField(fieldInfo: FieldInfo, mv: MethodVisitor) {
        fieldInfo.signature?.let {
            val typedDescriptor = it.substring(it.indexOf("<") + 1, it.indexOf(">"))
            println("typeparamer is $typedDescriptor")
            if (BuiltInTypeMap.containsKey(typedDescriptor)) {
                mv.visitVarInsn(Opcodes.ALOAD, 0)
                mv.visitTypeInsn(Opcodes.NEW, "java/util/ArrayList")
                mv.visitInsn(Opcodes.DUP)
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false)
                mv.visitFieldInsn(Opcodes.PUTFIELD, classInfo.name, fieldInfo.name, fieldInfo.descriptor)
                mv.visitVarInsn(Opcodes.ALOAD, 1)
                mv.visitVarInsn(Opcodes.ALOAD, 0)
                mv.visitFieldInsn(Opcodes.GETFIELD, classInfo.name, fieldInfo.name, fieldInfo.descriptor)
                mv.visitLdcInsn(Type.getType(typedDescriptor))
                mv.visitMethodInsn(
                    Opcodes.INVOKEVIRTUAL,
                    "java/lang/Class",
                    "getClassLoader",
                    "()Ljava/lang/ClassLoader;",
                    false
                )
                mv.visitMethodInsn(
                    Opcodes.INVOKEVIRTUAL,
                    "android/os/Parcel",
                    "readList",
                    "(Ljava/util/List;Ljava/lang/ClassLoader;)V",
                    false
                )
            } else {
                mv.visitFieldInsn(
                    Opcodes.GETSTATIC,
                    Type.getType(typedDescriptor).internalName,
                    "CREATOR",
                    PARCELABLE_CREATOR_DESCRIPTOR
                )
                mv.visitMethodInsn(
                    Opcodes.INVOKEVIRTUAL,
                    "android/os/Parcel",
                    "createTypedArrayList",
                    "($PARCELABLE_CREATOR_DESCRIPTOR)Ljava/util/ArrayList;",
                    false
                )
                mv.visitFieldInsn(Opcodes.PUTFIELD, classInfo.name, fieldInfo.name, fieldInfo.descriptor)
            }
        }
    }


    private fun generateParcelConstructorObjectField(fieldInfo: FieldInfo, mv: MethodVisitor) {
        if (BuiltInTypeMap.containsKey(fieldInfo.descriptor)) {
            mv.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                "android/os/Parcel",
                "read${BuiltInTypeMap[fieldInfo.descriptor]}",
                "()${fieldInfo.descriptor}",
                false
            )
            mv.visitFieldInsn(Opcodes.PUTFIELD, classInfo.name, fieldInfo.name, fieldInfo.descriptor)
        } else {
            mv.visitLdcInsn(Type.getType(fieldInfo.descriptor))
            mv.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                "java/lang/Class",
                "getClassLoader",
                "()Ljava/lang/ClassLoader;",
                false
            )
            mv.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                "android/os/Parcel",
                "readParcelable",
                "(Ljava/lang/ClassLoader;)$PARCELABLE_DESCRIPTOR",
                false
            )
//            mv.visitTypeInsn(Opcodes.CHECKCAST, fieldInfo.descriptor)
            mv.visitTypeInsn(Opcodes.CHECKCAST, Type.getType(fieldInfo.descriptor).internalName)
            mv.visitFieldInsn(Opcodes.PUTFIELD, classInfo.name, fieldInfo.name, fieldInfo.descriptor)
        }

    }


}