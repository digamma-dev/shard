import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("org.jetbrains.changelog") version "2.5.0"
    id("org.jetbrains.intellij.platform") version "2.18.1"
    id("org.jetbrains.kotlin.jvm") version "2.4.0"
}

group = providers.gradleProperty("plugin.group").get()
version = providers.gradleProperty("plugin.version").get()

kotlin {
    jvmToolchain(25)
}

repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdea(providers.gradleProperty("platform.version"))
        testFramework(TestFrameworkType.Platform)
    }
}

intellijPlatform {
    pluginConfiguration {
        id = providers.gradleProperty("plugin.id")
        name = providers.gradleProperty("plugin.name")
        description = providers.fileContents(layout.projectDirectory.file("DESCRIPTION.md"))
            .asText.map(::markdownToHTML)

        vendor {
            name = providers.gradleProperty("plugin.vendor.name")
        }

        ideaVersion {
            sinceBuild = providers.gradleProperty("plugin.since.build")
            untilBuild = provider { null }
        }
    }
}

changelog {
    header = provider { version.get() }
}
