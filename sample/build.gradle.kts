import com.acterics.dependencygraph.gradle.ApkDependencyGraphPluginExtension
import com.android.build.gradle.AppExtension

buildscript {
    repositories {
        mavenLocal()
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.2.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.21")
        classpath("org.jetbrains.kotlin:kotlin-android-extensions:1.3.21")
        classpath("com.acterics:apk-dependency-graph-generator:0.11.4")
    }
}

apply {
    plugin("android")
    plugin("kotlin-android")
    plugin("kotlin-android-extensions")
    plugin("com.acterics.apk-dependency-graph-generator")
}

repositories {
    mavenCentral()
    google()
    jcenter()
}

configure<AppExtension> {
    compileSdkVersion(28)
    buildToolsVersion("28.0.3")
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
    }

    sourceSets {
        val kotlinAddintionalSourceSets = project.file("src/main/kotlin")
        findByName("main")?.java?.srcDirs(kotlinAddintionalSourceSets)
    }
}

configure<ApkDependencyGraphPluginExtension> {
    filterPackage = "com.acterics"
    includeInnerClasses = false
    apkPath = "${project.buildDir.absolutePath}/outputs/apk/debug/sample-debug.apk"
}

dependencies {
    "implementation"("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.21")
    "implementation"("com.android.support:appcompat-v7:28.0.0")
    "implementation"("com.android.support:support-v4:28.0.0")
}

