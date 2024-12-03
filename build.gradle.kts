import org.jetbrains.kotlin.gradle.targets.js.dsl.WebpackRulesDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

group = "me.phillip"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
    js {
        browser {
            binaries.executable()
            fun WebpackRulesDsl.configure() {
                cssSupport {
                    enabled = true
                }
            }
            webpackTask {
                configure()
            }
            runTask {
                configure()
            }
        }
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(libs.kotlinxHtml)
            }
        }
    }
}