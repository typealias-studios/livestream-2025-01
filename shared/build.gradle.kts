
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.serialization)
    alias(libs.plugins.ksp)
}

kotlin {
    jvm()
    
    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktor.serialization.json)
            implementation(libs.arrow.optics)
        }
    }
}

dependencies {
    add("ksp", libs.arrow.optics.ksp)
}
