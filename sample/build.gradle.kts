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
    plugin("kotlin-kapt")
    plugin("com.acterics.apk-dependency-graph-generator")
}

repositories {
    mavenCentral()
    google()
    jcenter()
    maven(url = "https://jitpack.io")
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

    lintOptions {
        isAbortOnError = false
    }
}

configure<ApkDependencyGraphPluginExtension> {
    filterPackage = "com.acterics"
    includeInnerClasses = false
    apkPath = "${project.buildDir.absolutePath}/outputs/apk/debug/sample-debug.apk"
}

dependencies {
    "implementation"("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.21")
    "implementation"("androidx.appcompat:appcompat:1.0.2")

    "implementation"("com.github.terrakok:cicerone:5.0.0")

    "implementation"("tech.schoolhelper:moxy-x:1.7.0")
    "implementation"("tech.schoolhelper:moxy-x-androidx:1.7.0")
    "kapt"("tech.schoolhelper:moxy-x-compiler:1.7.0")

    "implementation"("io.reactivex.rxjava2:rxjava:2.2.7")
    "implementation"("io.reactivex.rxjava2:rxandroid:2.1.1")
    "implementation"("io.reactivex.rxjava2:rxkotlin:2.3.0")

    "implementation"("com.github.stephanenicolas.toothpick:toothpick-runtime:1.1.3")
    "kapt"("com.github.stephanenicolas.toothpick:toothpick-compiler:1.1.3")

}

