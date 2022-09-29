package app.include.build.appversion

import org.gradle.api.GradleException
import java.io.File
import java.util.*

class AppVersion(
    private val fileNaming: String,
    private val major: Int,
    private val minor: Int,
    private val patch: Int,
    private val buildVersionFile: File?,
    private val isBuild: Boolean
) {

    companion object {
        private const val PROPERTIES_VERSION_BUILD = "VERSION_BUILD"
        private const val DEFAULT_VERSION_BUILD = "0"
        private const val DEFAULT_ENVIRONMENT = "Unknown"

        fun requireVersionRange(errName: String, version: Int) {
            if (version < 0 || version > 99) {
                throw IllegalArgumentException("Wrong $errName version $version, must be in 0..99")
            }
        }
    }

    private val config by lazy { EnvironmentConfig() }
    private val localVersionBuild by lazy { readVersionBuildFile() }


    init {
        requireVersionRange("major", major)
        requireVersionRange("minor", minor)
        requireVersionRange("patch", patch)
    }

    fun versionCode(): Int {
        return major * 10000 + minor * 100 + patch
    }

    fun baseVersionName(): String {
        return "${major}.${minor}.${patch}"
    }

    fun versionNameEnvironmentSuffix(): String {
        val versionBuild = config.versionBuild ?: localVersionBuild
        return ".${versionBuild}_by_${getEnvironment()}"
    }

    fun archivesBaseName(): String {
        val baseVersion = baseVersionName()
        val envSuffix = versionNameEnvironmentSuffix()
        return "${fileNaming}_${baseVersion}${envSuffix}"
    }

    private fun getEnvironment(): String {
        val envType = config.envType ?: DEFAULT_ENVIRONMENT
        val envName = config.envName
        return if (envName != null) {
            "$envName[$envType]"
        } else {
            envType
        }
    }

    private fun readVersionBuildFile(): String {
        val file = buildVersionFile ?: return DEFAULT_VERSION_BUILD
        if (!file.exists()) {
            file.createNewFile()
        }
        if (!file.canRead()) {
            throw GradleException("Can't read version file")
        }

        val properties = Properties()
        properties.load(file.inputStream())

        val propertyVersion = properties.getProperty(PROPERTIES_VERSION_BUILD)?.toShortOrNull() ?: 0
        val versionBuild = if (isBuild) {
            (propertyVersion + 1).toString().also {
                properties.setProperty(PROPERTIES_VERSION_BUILD, it)
                properties.store(file.writer(), null)
            }
        } else {
            propertyVersion.toString()
        }

        return versionBuild
    }
}

