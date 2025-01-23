
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.serialization)
}

kotlin {
    jvm()
    
    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktor.serialization.json)
        }
    }
}
