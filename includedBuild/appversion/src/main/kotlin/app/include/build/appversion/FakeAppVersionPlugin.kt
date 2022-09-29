package app.include.build.appversion

import org.gradle.api.Plugin
import org.gradle.api.Project

class FakeAppVersionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        // Do nothing, just load dependencies to classpath
    }
}