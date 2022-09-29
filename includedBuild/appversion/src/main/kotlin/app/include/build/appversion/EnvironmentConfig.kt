package app.include.build.appversion

internal class EnvironmentConfig {

    var isCi: Boolean = false
        private set
    var envType: String? = "Unknown"
        private set
    var envName: String? = "Unknown"
        private set
    var versionBuild: String? = null
        private set

    init {
        isCi = System.getenv("CI")?.toBoolean() ?: isCi
        if (isCi) {
            val githubVersionBuild = System.getenv("GITHUB_RUN_NUMBER")
            val bitbucketVersionBuild = System.getenv("BITBUCKET_BUILD_NUMBER")

            envType = "CI"
            if (githubVersionBuild != null) {
                envName = "GitHub"
                versionBuild = githubVersionBuild
            }
            if (bitbucketVersionBuild != null) {
                envName = "Bitbucket"
                versionBuild = bitbucketVersionBuild
            }
        } else {
            val ideaName = System.getProperty("idea.platform.prefix")
            val userName = System.getProperty("user.name")

            envType = ideaName ?: "Console"
            envName = userName ?: "Unknown"
        }
    }
}

