import org.jreleaser.gradle.plugin.tasks.JReleaserDeployTask
import org.jreleaser.model.Active
import org.jreleaser.model.Signing

plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
    id("org.jreleaser")
}

group = "io.github.nullpops"
version = "1.0.2"

java {
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform(Testing.junit.bom))
    testImplementation(Testing.junit.jupiter)
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JReleaserDeployTask> {
    dependsOn("publish")
}


publishing {
    repositories {
        maven {
            name = "staging"
            url = uri("${layout.buildDirectory.asFile.get().path }/staging-deploy")
        }
    }
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                name.set("logger")
                description.set("A lightweight, expressive logger written in Kotlin")
                url.set("https://github.com/nullpops/logger")
                licenses {
                    license {
                        name.set("AGPL-3.0-only")
                        url.set("https://www.gnu.org/licenses/agpl-3.0.html")
                        distribution.set("repo")
                    }
                    license {
                        name.set("NullPops Commercial License")
                        url.set("https://github.com/NullPops/logger/blob/main/LICENSE")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("zeruth")
                        name.set("Tyler Bochard")
                        email.set("tylerbochard@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/nullpops/logger.git")
                    developerConnection.set("scm:git:ssh://github.com/nullpops/logger.git")
                    url.set("https://github.com/nullpops/logger")
                }
            }
        }
    }
}

jreleaser {
    signing {
        dryrun = false
        active.set(Active.ALWAYS)
        armored.set(true)
        mode = Signing.Mode.MEMORY
        providers.environmentVariable("JRELEASER_GPG_PUBLIC_KEY_PATH").orNull?.let {
            publicKey.set(file(it).readText())
        }
        providers.environmentVariable("JRELEASER_GPG_PRIVATE_KEY_PATH").orNull?.let {
            secretKey.set(file(it).readText())
        }

        passphrase.set(providers.environmentVariable("JRELEASER_GPG_PASSPHRASE"))
    }
    deploy {
        maven {
            mavenCentral {
                create("logger") {
                    active.set(Active.ALWAYS)
                    url.set("https://central.sonatype.com/api/v1/publisher")
                    stagingRepository("build/staging-deploy")
                }
            }
        }
    }
}