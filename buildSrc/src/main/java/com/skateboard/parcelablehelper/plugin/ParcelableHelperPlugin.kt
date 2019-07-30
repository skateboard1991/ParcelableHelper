package com.skateboard.parcelablehelper.plugin

import com.android.build.gradle.AppExtension
import com.skateboard.parcelablehelper.transform.ParcelableTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

class ParcelableHelperPlugin : Plugin<Project> {

    val API_CONFIGURATION="api"

    val ANNOATION_DEPENDENCY=""

    override fun apply(project: Project) {

        val apiConfiguration=project.configurations.getByName("api")

        apiConfiguration.dependencies.add(project.dependencies.create(ANNOATION_DEPENDENCY))

        val android = project.extensions.findByType(AppExtension::class.java)

        android?.registerTransform(ParcelableTransform(project))


    }
}