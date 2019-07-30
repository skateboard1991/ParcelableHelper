package com.skateboard.parcelablehelper.util

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

object FileUtil {


    fun writeToFile(inputFile: File, content: ByteArray) {

        try {
            val outputStream = FileOutputStream(inputFile)
            outputStream.write(content)
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    fun forceDelete(file:File){

        if(file.isDirectory){
            file.deleteRecursively()
        }
        else{
            file.delete()
        }
    }


    fun forceMkdir(file:File){

        if(file.exists()){
            return
        }
        else{
            file.mkdirs()
        }
    }



    fun writeToFile(inputFile: File, outputFile: File) {

        try {
            val inputStream = FileInputStream(inputFile)

            val outputStream = FileOutputStream(outputFile)

            var data = inputStream.read()

            while (data != -1) {

                outputStream.write(data)

                data = inputStream.read()
            }

            inputStream.close()

            outputStream.close()
        } catch (e: IOException) {

            e.printStackTrace()
        }

    }
}