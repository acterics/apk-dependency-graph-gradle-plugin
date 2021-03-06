import com.gradle.publish.PluginBundleExtension

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
    id("com.gradle.plugin-publish") version "0.10.0"
    id("nu.studer.credentials") version "1.0.7"
}

version = "0.12.0"
group = "com.acterics"

repositories {
   mavenCentral()
}

dependencies {
   "implementation"("org.smali:baksmali:2.2.6")
   "implementation"("com.google.guava:guava:24.1-jre")
}

configure<PublishingExtension> {
    publications {
        create("pluginPublication", MavenPublication::class) {
            from(components.findByName("java"))
            groupId = project.group.toString()
            version = project.version.toString()
            artifactId = "apk-dependency-graph-generator"
        }
    }
}

configure<PluginBundleExtension> {
   website = "https://github.com/acterics/apk-dependency-graph-gradle-plugin"
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
      }
   }
}

afterEvaluate {
   tasks["build"].dependsOn(parent!!.tasks["copyWebBundleToCoreResources"])
}
