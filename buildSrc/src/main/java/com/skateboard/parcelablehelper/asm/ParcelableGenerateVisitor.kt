package com.skateboard.parcelablehelper.asm

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

    private var builtInTypeMap = mutableMapOf<String, String>()

    companion object {

        val PARCELABLE_ANNOATION_NAME = "Lcom/skateboard/parcelableannoation/Parcelable;"

        val PARCELABLE_IGNORE_ANNOATION_NAME = "Lcom/skateboard/parcelableannoation/Ignore"

    }


    init {
        prepareBuiltInTypeMap()
    }

    private fun prepareBuiltInTypeMap() {
        builtInTypeMap["B"] = "Byte"
        builtInTypeMap["[B"] = "ByteArray"
        builtInTypeMap["I"] = "Int"
        builtInTypeMap["[I"] = "IntArray"
        builtInTypeMap["J"] = "Long"
        builtInTypeMap["[J"] = "LongArray"
        builtInTypeMap["Z"] = "Boolean"
        builtInTypeMap["[Z"] = "BooleanArray"
        builtInTypeMap["F"] = "Float"
        builtInTypeMap["[F"] = "FloatArray"
        builtInTypeMap["D"] = "Double"
        builtInTypeMap["[D"] = "DoubleArray"
        builtInTypeMap["Ljava/lang/Byte;"] = "Byte"
        builtInTypeMap["[Ljava/lang/Byte;"] = "ByteArray"
        builtInTypeMap["Ljava/lang/Integer;"] = "Int"
        builtInTypeMap["[Ljava/lang/Integer;"] = "IntArray"
        builtInTypeMap["Ljava/lang/Long;"] = "Long"
        builtInTypeMap["[Ljava/lang/Long;"] = "LongArray"
        builtInTypeMap["Ljava/lang/Boolean;"] = "Boolean"
        builtInTypeMap["[Ljava/lang/Boolean;"] = "BooleanArray"
        builtInTypeMap["Ljava/lang/Float;"] = "Float"
        builtInTypeMap["[Ljava/lang/Float;"] = "FloatArray"
        builtInTypeMap["Ljava/lang/Double;"] = "Double"
        builtInTypeMap["[Ljava/lang/Double;"] = "DoubleArray"
        builtInTypeMap["Ljava/lang/String;"] = "String"
        builtInTypeMap["[Ljava/lang/String;"] = "StringArray"
        builtInTypeMap["Ljava/io/Serializable;"] = "Serializable"
        builtInTypeMap["Landroid/os/Parcelable"] = "Parcelable"
    }

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
            if (PARCELABLE_IGNORE_ANNOATION_NAME == descriptor) {
                fieldList[fieldList.size - 1].isIgnore = true
            }
            return super.visitAnnotation(descriptor, visible)
        }
    }


    override fun visitEnd() {

        if (isIntrested) {
            generateParcelableImplemation()
            generateCreator()
            generateDescribeContent()
            generateWriteToParcel()
            generateParcelConstructor()
            generateCreatorInnerClass()
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
        newInterfaces[newInterfaces.size - 1] = "android/os/Parcelable"
        super.visit(
            classInfo.version,
            classInfo.access,
            classInfo.name,
            classInfo.signature,
            classInfo.superName,
            newInterfaces
        )
    }

    private fun generateCreator() {

        cv.visitInnerClass("${classInfo.name}${'$'}1", null, null, Opcodes.ACC_STATIC)
        cv.visitInnerClass(
            "android/os/Parcelable${'$'}Creator",
            "android/os/Parcelable",
            "Creator",
            Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE
        )

        cv.visitField(
            Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC,
            "CREATOR",
            "Landroid/os/Parcelable${'$'}Creator;",
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
            "Landroid/os/Parcelable${'$'}Creator;"
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

        if (builtInTypeMap.containsKey(fieldInfo.descriptor)) {
            mv.visitFieldInsn(Opcodes.GETFIELD, classInfo.name, fieldInfo.name, fieldInfo.descriptor)
            mv.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                "android/os/Parcel",
                "write${builtInTypeMap[fieldInfo.descriptor]}",
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
                "([Landroid/os/Parcelable;I)V",
                false
            )
        }

    }


    private fun generateWriteToParcelListField(fieldInfo: FieldInfo, mv: MethodVisitor) {

        fieldInfo.signature?.let {
            val typedDescriptor = it.substring(it.indexOf("<") + 1, it.indexOf(">"))
            println("typeparamer is $typedDescriptor")
            if (builtInTypeMap.containsKey(typedDescriptor)) {
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

        if (builtInTypeMap.containsKey(fieldInfo.descriptor)) {

            mv.visitFieldInsn(
                Opcodes.GETFIELD,
                classInfo.name,
                fieldInfo.name,
                fieldInfo.descriptor
            )
            mv.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                "android/os/Parcel",
                "write${builtInTypeMap[fieldInfo.descriptor]}",
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
                "(Landroid/os/Parcelable;I)V",
                false
            )
        }
    }


    private fun generateParcelConstructor() {
        val mv = cv.visitMethod(Opcodes.ACC_PROTECTED, "<init>", "(Landroid/os/Parcel;)V", null, null)
        mv.visitCode()
        mv.visitVarInsn(Opcodes.ALOAD, 0)
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
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
        if (builtInTypeMap.containsKey(fieldInfo.descriptor)) {

            mv.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                "android/os/Parcel",
                "create${builtInTypeMap[fieldInfo.descriptor]}",
                "()${fieldInfo.descriptor}",
                false
            )
            mv.visitFieldInsn(Opcodes.PUTFIELD, classInfo.name, fieldInfo.name, fieldInfo.descriptor)
        } else {
            mv.visitFieldInsn(
                Opcodes.GETSTATIC,
                Type.getType(fieldInfo.descriptor?.substring(1)).internalName,
                "CREATOR",
                "Landroid/os/Parcelable${'$'}Creator;"
            )
            mv.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                "android/os/Parcel",
                "createTypedArray",
                "(Landroid/os/Parcelable${'$'}Creator;)[Ljava/lang/Object;",
                false
            )
            mv.visitTypeInsn(Opcodes.CHECKCAST, Type.getType(fieldInfo.descriptor).internalName)
            mv.visitFieldInsn(Opcodes.PUTFIELD, classInfo.name, fieldInfo.name, "${fieldInfo.descriptor}")
        }
    }

    private fun generateParcelConstructorListField(fieldInfo: FieldInfo, mv: MethodVisitor) {
        fieldInfo.signature?.let {
            val typedDescriptor = it.substring(it.indexOf("<") + 1, it.indexOf(">"))
            println("typeparamer is $typedDescriptor")
            if (builtInTypeMap.containsKey(typedDescriptor)) {
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
                    "Landroid/os/Parcelable${'$'}Creator;"
                )
                mv.visitMethodInsn(
                    Opcodes.INVOKEVIRTUAL,
                    "android/os/Parcel",
                    "createTypedArrayList",
                    "(Landroid/os/Parcelable${'$'}Creator;)Ljava/util/ArrayList;",
                    false
                )
                mv.visitFieldInsn(Opcodes.PUTFIELD, classInfo.name, fieldInfo.name, fieldInfo.descriptor)
            }
        }
    }


    private fun generateParcelConstructorObjectField(fieldInfo: FieldInfo, mv: MethodVisitor) {
        if (builtInTypeMap.containsKey(fieldInfo.descriptor)) {
            mv.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                "android/os/Parcel",
                "read${builtInTypeMap[fieldInfo.descriptor]}",
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
                "(Ljava/lang/ClassLoader;)Landroid/os/Parcelable;",
                false
            )
            mv.visitTypeInsn(Opcodes.CHECKCAST, Type.getType(fieldInfo.descriptor).internalName)
            mv.visitFieldInsn(Opcodes.PUTFIELD, classInfo.name, fieldInfo.name, fieldInfo.descriptor)
        }

    }


}