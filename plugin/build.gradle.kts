import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    id("java")
    id("xyz.jpenilla.run-paper")
    id("de.eldoria.plugin-yml.paper")
}

repositories {
    maven {
        name = "fantasyrealmsOtherLibraries"
        url = uri("https://repo.fantasyrealms.net/other-libraries")
    }
    maven("https://jitpack.io")
}

dependencies {
    implementation(project(":shared"))

    val lamp = "4.0.0-rc.+"
    implementation("io.github.revxrsal:lamp.common:${lamp}")
    implementation("io.github.revxrsal:lamp.bukkit:${lamp}")

    var configLib = "4.+"
    implementation("de.exlll:configlib-yaml:${configLib}")
    implementation("de.exlll:configlib-paper:${configLib}")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.shadowJar {
    archiveClassifier.set("")
    archiveBaseName.set(rootProject.name)
}

val shadowDevJar by tasks.registering(ShadowJar::class) {
    description = "Create a unrelocated JAR for run-paper"

    from(sourceSets.main.map { it.output })
    configurations = project.configurations.runtimeClasspath.map { listOf(it) }

    archiveClassifier = "dev"
    archiveBaseName.set(rootProject.name)
}

tasks.test {
    useJUnitPlatform()
}

runPaper {
    disablePluginJarDetection()
}

tasks {
    runServer {
        dependsOn(shadowDevJar)
        pluginJars(shadowDevJar.get().archiveFile)

        minecraftVersion("1.21.11")
        runDirectory(project.projectDir.resolve("runServer"))

        downloadPlugins {
            modrinth("tabtps", "YvY9J2Wb")
        }
    }
}

paper {
    name = "SimpleJumpPads"
    description = "Simple and feature packed jump pads plugin for your server!"
    website = "https://happyareabean.cc"
    authors = listOf("HappyAreaBean")

    main = "cc.happyareabean.jumppad.SimpleJumpPads"
    apiVersion = "1.19"

    defaultPermission = BukkitPluginDescription.Permission.Default.OP

    generateLibrariesJson = false
}

tasks.withType(xyz.jpenilla.runtask.task.AbstractRun::class) {
    javaLauncher = javaToolchains.launcherFor {
        vendor = JvmVendorSpec.JETBRAINS
        languageVersion = JavaLanguageVersion.of(21)
    }
    jvmArgs("-XX:+AllowEnhancedClassRedefinition")
}