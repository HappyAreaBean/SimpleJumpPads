plugins {
    id("java")
    id("io.freefair.lombok") version "9.2.0" apply false
    id("xyz.jpenilla.run-paper") version "3.0.2" apply false
    id("com.gradleup.shadow") version "9.4.1" apply false
    id("de.eldoria.plugin-yml.paper") version "0.9.0" apply false
}

group = "cc.happyareabean"
version = "1.0.2"

subprojects {
    apply(plugin = "java")
    apply(plugin = "com.gradleup.shadow")
    apply(plugin = "io.freefair.lombok")

    version = getProjectVersion()

    repositories {
        mavenCentral()
        maven {
            name = "papermc-repo"
            url = uri("https://repo.papermc.io/repository/maven-public/")
        }
    }

    val targetJavaVersion = 21
    java {
        val javaVersion = JavaVersion.toVersion(targetJavaVersion)
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
        if (JavaVersion.current() < javaVersion) {
            toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
        }
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-parameters")

        if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
            options.release.set(targetJavaVersion)
        }
    }

    tasks.processResources {
        val props = mapOf("version" to version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    dependencies {
        compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")
    }
}

fun getProjectVersion(): String {
    return rootProject.version.toString()
}
