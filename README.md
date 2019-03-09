# Apk dependency graph generator

Gradle plugin for generating apk dependency graph

## Usage

In your app `build.gradle`

``` groovy
apply plugin: 'apk-dependency-graph-generator'
```

Then run

``` bash
./gradlew app:analyzeSmali
```

Finally open `$buildSrc/apk-dependency-graph/index.html`

## Credits

Thanks to [Alex Zaitsev](https://github.com/alexzaitsev) and his [apk-dependency-graph](https://github.com/alexzaitsev/apk-dependency-graph). This project rewrites his library to Kotlin and wrap in gradle plugin.