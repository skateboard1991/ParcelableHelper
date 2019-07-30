package com.skateboard.parcelablehelper.asm

import com.skateboard.parcelablehelper.util.FileUtil
import org.apache.commons.io.FileUtils
import java.io.File


/**
 * 获取目录下class文件并调用asm解析
 * */
object ParcelableByteCodeUtil {


    fun transformDirectoryInput(input: File, output: File) {

        if (output.exists()) {
            FileUtil.forceDelete(output)
        }
        FileUtil.forceMkdir(output)
        val srcDirPath = input.absolutePath
        val destDirPath = output.absolutePath
        println("=== transform dir = $srcDirPath, $destDirPath")
        if (input.isDirectory) {
            input.listFiles().forEach {
                val destFilePath = it.absolutePath.replace(srcDirPath, destDirPath)
                val destFile = File(destFilePath)
                if (it.isDirectory) {
                    transformDirectoryInput(it, destFile)
                } else if (it.isFile) {
                    FileUtils.touch(destFile)
                    performClassParse(it, destFile)
                }
            }
        }

    }

    private fun performClassParse(inputFile: File, outputFile: File) {
        if (inputFile.name.endsWith(".class") && inputFile.name != "R.class" && inputFile.name != "BuildConfig.class" && !inputFile.name.startsWith("R$")) {
            println("dst name is ${inputFile.name}")
            ParcelableParser.parseClass(inputFile, outputFile)
        } else {
            FileUtil.writeToFile(inputFile, outputFile)
        }
    }

}