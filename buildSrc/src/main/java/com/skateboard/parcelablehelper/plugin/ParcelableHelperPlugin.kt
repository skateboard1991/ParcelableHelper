package com.skateboard.parcelablehelper.plugin

import com.android.build.gradle.AppExtension
import com.skateboard.parcelablehelper.transform.ParcelableTransform
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.ArtifactRepository
import java.net.URI

class ParcelableHelperPlugin : Plugin<Project> {

    val API_CONFIGURATION = "api"

    val ANNOATION_DEPENDENCY = "com.skateboard.parcelablehelper:parcelableannoation:1.0.0"

    val JCENTER_URL = "https://dl.bintray.com/wuhaoxuan1225/maven/"

    override fun apply(project: Project) {

        addAnnoationDependency(project)

        val android = project.extensions.findByType(AppExtension::class.java)

        android?.registerTransform(ParcelableTransform(project))


    }


    private fun addAnnoationDependency(project: Project) {

        project.allprojects.forEach { project ->
            project.repositories.maven {
                it.url = URI.create(JCENTER_URL)
            }

            project.buildscript.repositories.maven{
                it.url = URI.create(JCENTER_URL)
            }
        }

        val apiConfiguration = project.configurations.getByName(API_CONFIGURATION)

        apiConfiguration.dependencies.add(project.dependencies.create(ANNOATION_DEPENDENCY))
    }

}