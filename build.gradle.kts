import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML

plugins {
    id("org.jetbrains.changelog") version "2.5.0"
    id("org.jetbrains.intellij.platform") version "2.17.0"
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
    }
}

intellijPlatform {
    pluginConfiguration {
        id = providers.gradleProperty("plugin.id")
        name = providers.gradleProperty("plugin.name")
        version = providers.gradleProperty("plugin.version")

        description = providers.fileContents(layout.projectDirectory.file("DESCRIPTION.md")).asText
            .map(::markdownToHTML)

        changeNotes = provider {
            changelog.renderItem(
                (changelog.getOrNull(version.get()) ?: changelog.getUnreleased())
                    .withHeader(false)
                    .withEmptySections(false),
                Changelog.OutputType.HTML
            )
        }

        vendor {
            name = providers.gradleProperty("plugin.vendor.name")
        }

        ideaVersion {
            sinceBuild = providers.gradleProperty("plugin.since.build")
            untilBuild = provider { null }
        }
    }

    pluginVerification {
        ides {
            recommended()
        }
    }
}
