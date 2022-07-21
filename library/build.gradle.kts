plugins {
    id("com.android.library")
}

repositories {
    google()
    mavenCentral()
}

dependencies {
   implementation("com.android.support:support-v4:28.0.0")
    implementation("com.android.support:support-annotations:28.0.0")
    implementation("com.android.support:recyclerview-v7:28.0.0")
}

android {
    compileSdkVersion(Plugins.compile)
    buildToolsVersion(Plugins.buildVersion)
}

//apply from: '../maven_push.gradle'
