package com.skateboard.parcelablehelper.transform

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import com.skateboard.parcelablehelper.asm.ParcelableByteCodeUtil
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.Project

/**
 * 用来获取class文件的输入与输出
 * */
class ParcelableTransform(private val project: Project) : Transform() {
    override fun getName(): String = "ParcelableTransform"

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> =
        TransformManager.CONTENT_CLASS

    override fun isIncremental(): Boolean = true

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> = TransformManager.PROJECT_ONLY


    override fun transform(transformInvocation: TransformInvocation?) {

        println("----------------进入ParcelableHelpe Transform--------------")
        transformInvocation?.let { transformInvocation ->
            //遍历input
            transformInvocation.inputs.forEach { transformInput ->
                //遍历文件夹
                transformInput.directoryInputs.forEach { directoryInput ->
                    // 获取output目录
                    val dest = transformInvocation.outputProvider.getContentLocation(
                        directoryInput.name,
                        directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY
                    )

                    val inputPath = directoryInput.file.absolutePath
                    ParcelableByteCodeUtil.transformDirectoryInput(directoryInput.file, dest)
                }

            }
        }
        println("--------------结束进入ParcelableHelpe Transform----------------")
    }

}