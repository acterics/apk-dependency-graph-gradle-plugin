import com.gradle.publish.PluginBundleExtension

plugins {
   `kotlin-dsl`
   `java-gradle-plugin`
   id("com.gradle.plugin-publish") version "0.10.0"
}

repositories {
   mavenCentral()
}

dependencies {
   "implementation"("org.smali:baksmali:2.2.6")
   "implementation"("com.google.guava:guava:24.1-jre")
}

configure<PluginBundleExtension> {
   website = "https://github.com/acterics"
   vcsUrl = "https://github.com/acterics/apk-dependency-graph-gradle-plugin"
   tags = listOf("android", "apk-dependency-graph")
}

configure<GradlePluginDevelopmentExtension> {
   plugins {
      create("ApkDependencyGraphGenerator") {
         id = "com.acterics.apk-dependency-graph-generator"
         displayName = "Apk dependency graph generator"
         description = "Gradle plugin for visualization your apk dependency graph"
         implementationClass = "com.acterics.dependencygraph.gradle.ApkDependencyGraphPlugin"
         version = "0.11.0"
      }
   }
}

afterEvaluate {
   tasks["build"].dependsOn(parent!!.tasks["copyWebBundleToCoreResources"])
}
