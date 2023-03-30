plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.numq.draganddrop"
version = "0.0.1"

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
            }
        }
    }
}