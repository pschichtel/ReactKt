import org.jetbrains.kotlin.gradle.targets.js.dsl.WebpackRulesDsl

plugins {
    alias(libs.plugins.kotlinJs)
}

group = "me.phillip"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
dependencies {
    implementation(libs.kotlinxHtml)
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
}