rootProject.name = "apk-dependency-graph-gradle-plugin"

include("web")
include("plugin")
include("sample")

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        maven { setUrl("https://jcenter.bintray.com/") }
        maven { setUrl("https://dl.bintray.com/kotlin/kotlin-eap") }
    }

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
//                "kotlin" -> useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
                "kotlin2js" -> useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
                "kotlin-dce-js" -> useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
                "org.jetbrains.kotlin.frontend" -> useModule("org.jetbrains.kotlin:kotlin-frontend-plugin:${requested.version}")
            }
        }
    }
}