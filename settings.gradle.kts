rootProject.name = "SimpleJumpPad"

include("shared")
include("plugin")

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}