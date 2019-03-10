
plugins {
   `kotlin-dsl`
//   kotlin("jvm") version "1.3.21"
//   `java-gradle-plugin`
//   id("com.gradle.plugin-publish") version "0.10.0"
}




repositories {
   mavenCentral()
//   maven { url = URI("https://repo.gradle.org/gradle/libs-releases-local/")}
}

dependencies {
//   compileOnly(gradleApi())
//   implementation("org.gradle:gradle-kotlin-dsl:1.1.3")
//   implementation(kotlin("stdlib"))
   "implementation"("org.smali:baksmali:2.2.6")
   "implementation"("com.google.guava:guava:24.1-jre")
}

//pluginBundle {
//   website = "https://github.com/acterics"
//   vcsUrl = "https://github.com/acterics/apk-dependency-graph-gradle-plugin"
//   tags = listOf("android", "apk-dependency-graph")
//}
//
//gradlePlugin {
//   plugins {
//       create("ApkDependencyGraphGenerator") {
//           id = "com.acterics.apk-dependency-graph-generator"
//           displayName = "Apk dependency graph generator"
//           description = "Gradle plugin for visualization your apk dependency graph"
//           implementationClass = "com.acterics.dependencygraph.gradle.ApkDependencyGraphPlugin"
//           version = "0.10.0"
//       }
//   }
//}

afterEvaluate {
   tasks["build"].dependsOn(parent!!.tasks["copyWebBundleToCoreResources"])
}
