import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.frontend.KotlinFrontendExtension
import org.jetbrains.kotlin.gradle.frontend.npm.NpmExtension
import org.jetbrains.kotlin.gradle.frontend.webpack.WebPackExtension
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile


plugins {
    id("kotlin2js") version "1.3.21"
    id("kotlin-dce-js") version "1.3.21"
    id("org.jetbrains.kotlin.frontend") version "0.0.45"
}

repositories {
   mavenCentral()
}

dependencies {
   implementation(kotlin("stdlib-js"))
}

kotlinFrontend {
    sourceMaps = true

    npm {
//        dependency("css-loader")
//        dependency("style-loader")
//        devDependency("karma")
    }

    bundle<WebPackExtension>("webpack") {
        (this as WebPackExtension).apply {
            port = 8080
            publicPath = "/frontend/"
            proxyUrl = "http://localhost:9000"
        }
    }

    define("PRODUCTION", true)
}



tasks {
    "compileKotlin2Js"(Kotlin2JsCompile::class) {
        kotlinOptions {
            metaInfo = true
            outputFile = "${project.buildDir.path}/js/scripts/output.js"
            sourceMap = true
            sourceMapEmbedSources = "always"
            moduleKind = "commonjs"
            main = "call"

       }
    }


}

task("copyResources", Copy::class) {
    from(file("$projectDir/src/main/resources"))
    into(file(buildDir.path + "/bundle"))
}

afterEvaluate {
    tasks["webpack-bundle"].dependsOn("copyResources")
    tasks["webpack-run"].dependsOn("copyResources")
    tasks["webpack-bundle"].dependsOn("runDceKotlinJs")
    tasks["webpack-run"].dependsOn("runDceKotlinJs")
}
