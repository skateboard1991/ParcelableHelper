package com.skateboard.parcelablehelper.asm

import com.skateboard.parcelablehelper.util.FileUtil
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassReader.SKIP_DEBUG
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.ClassWriter.COMPUTE_MAXS
import java.io.File
import java.io.FileInputStream

/**
 * 对class文件进行解析
 * */
object ParcelableParser {


    fun parseClass(classFile: File, outputFile: File) {
        val classInput = FileInputStream(classFile)
        val classReader = ClassReader(classInput)
        val classWriter = ClassWriter(COMPUTE_MAXS)
        val parcelableGenerateVisitor = ParcelableGenerateVisitor(classWriter)
        classReader.accept(parcelableGenerateVisitor, SKIP_DEBUG)
        FileUtil.writeToFile(outputFile, classWriter.toByteArray())
    }


}