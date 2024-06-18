import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinCocoapods)
    id("maven-publish")
}

val libVersion = "0.0.1"

group = "com.example.kmmlibrarypoc"
version = libVersion

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    androidTarget {
        publishLibraryVariants("release")
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    val iosFrameworkName = "shared"
    val xcf = XCFramework()
    val iosTargets = listOf(iosX64(), iosArm64(), iosSimulatorArm64())

    iosTargets.forEach {
        it.binaries.framework {
            baseName = iosFrameworkName
            xcf.add(this)
        }
    }

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = libVersion
        ios.deploymentTarget = "14.1"
        framework {
            baseName = iosFrameworkName
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                //put your multiplatform dependencies here
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}

android {
    namespace = "com.example.kmmlibrarypoc"
    compileSdk = 34
    defaultConfig {
        minSdk = 23
    }
}


publishing {
    publications {
        withType<MavenPublication> {
            groupId = "com.kingmakers.analytics"
        }

        create<MavenPublication>("iOSFramework") {
            artifactId = "shared-ios"
            artifact("$buildDir/cocoapods/publish/shared.zip")
        }

        repositories {
            maven {
                name="com.example.kmmlibrarypoc"
                url = uri("https://github.com/TonyAndroid1991/KMMLibraryPOC")
                credentials {
                    username = "kingmakersUser"
                    password = "kingmakersToken"
                }
                authentication {
                    create<BasicAuthentication>("basic")
                }
            }
        }
    }
}

//tasks.register<Zip>("buildIosFramework") {
//    dependsOn("podPublishReleaseXCFramework")
//    from(layout.buildDirectory.dir("cocoapods/publish/release/"))
//    destinationDirectory.set(layout.buildDirectory.dir("cocoapods/publish/"))
//    archiveFileName.set("shared.zip")
//}
//
//tasks.register("publishIosFrameworkToMaven") {
//    dependsOn("buildIosFramework")
//    dependsOn("publishIOSFrameworkPublicationToCom.kingmakers.analyticsRepository")
//    tasks.findByName("publishIOSFrameworkPublicationToCom.kingmakers.analyticsRepository")?.mustRunAfter("buildIosFramework")
//}


dependencies {
    implementation(project(":shared:features"))
}
