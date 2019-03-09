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


