buildscript {
    repositories{
        google()
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
        }
        // maven(url = Plugins.fabricMavenUrl)
    }

    dependencies {
        classpath(Plugins.androidGradlePlugin)
        classpath(Plugins.googleVersionGradlePlugin)
        classpath(Plugins.kotlinGradlePlugin)
        classpath(Plugins.mavenGradlePlugin)
        classpath(Plugins.hiltExtensions)
        classpath(Plugins.safeArgsPlugin)
        classpath("com.android.tools.build:gradle:7.2.1")
    }
}

plugins{
    id(Plugins.detekt).version("1.16.0")
}

allprojects{
    repositories{
        google()
        mavenCentral()
        jcenter()
        maven(url = Plugins.jitpackUrl)
        maven(url ="https://repo.spring.io/plugins-release/")
    }
}

task("clean"){
    delete(rootProject.buildDir)
}

val detektDeezer by tasks.registering(io.gitlab.arturbosch.detekt.Detekt::class) {
    description = "Run all deezer modules"
    buildUponDefaultConfig = true
    autoCorrect = true
    parallel = true
    setSource(files(projectDir))
    config.setFrom(files("$rootDir/detekt/config.yml"))
    include("**/*.kt")
    include("**/*.kts")
    exclude("**/build/**")
    exclude("**/buildSrc/**")
    exclude("**/test/**/*.kt")
    reports {
        xml.enabled = false
        html.enabled = false
        txt.enabled = false
    }
}


