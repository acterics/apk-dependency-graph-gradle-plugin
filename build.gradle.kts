plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "0.10.0"
}

configure<KotlinDslPluginOptions> {
    experimentalWarning.set(false)
}


repositories {
    mavenCentral()
}

dependencies {
    implementation("org.smali:baksmali:2.2.6")
    implementation("com.google.guava:guava:24.1-jre")
}

pluginBundle {
    website = "https://github.com/acterics"
    vcsUrl = "https://github.com/acterics/apk-dependency-graph-gradle-plugin"
    tags = listOf("android", "apk-dependency-graph")
}

gradlePlugin {
    plugins {
        create("ApkDependencyGraphGenerator") {
            id = "com.acterics.apk-dependency-graph-generator"
            displayName = "Apk dependency graph generator"
            description = "Gradle plugin for visualization your apk dependency graph"
            implementationClass = "com.acterics.wmu.gradle.ApkDependencyGraphPlugin"
            version = "0.9.0"
        }
    }
}

