import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.serialization)
    alias(libs.plugins.ksp)
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(projects.shared)

            implementation(libs.arrow.core)
            implementation(libs.arrow.fx.coroutines)
            implementation(libs.arrow.resilience)
            implementation(libs.arrow.optics)
            implementation(libs.arrow.optics.compose)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.serialization.json)
            implementation(libs.ktor.content.negotiation)
            implementation("io.coil-kt.coil3:coil-compose:3.0.4")
            implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.4")
            implementation("io.coil-kt.coil3:coil-compose:3.0.4")
            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.8.0-alpha10")
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.compose.ui.desktop)
            implementation(compose.material3)
        }
        desktopMain.kotlin {
            srcDir("build/generated/ksp/desktop/desktopMain/kotlin")
        }
    }
}

dependencies {
    add("kspDesktop", libs.arrow.optics.ksp)
}

compose.desktop {
    application {
        mainClass = "com.daveleeds.arrowdemo.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.daveleeds.arrowdemo"
            packageVersion = "1.0.0"
        }
    }
}
