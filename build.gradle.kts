plugins {
    `java-library`
    `maven-publish`

    id("io.papermc.paperweight.userdev") version "2.0.0-beta.21"
    id("maven-publish")
    id("xyz.jpenilla.run-paper") version "3.0.2"
}

repositories {
    mavenCentral()

    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle("26.1.2.build.+")
}

group = "me.hapyl"
version = "6.2.11-SNAPSHOT"
description = "EternaAPI"

// Set java settings, attach sources and javadocs
java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25

    withSourcesJar()
    withJavadocJar()
}

// Setup publishing to github
publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
        groupId = "me.hapyl"
        artifactId = "eternaapi"
        version = project.version.toString()
    }

    repositories {
        maven {
            name = "github"
            url = uri("https://maven.pkg.github.com/hapyl/eternaapi")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    // Silence javadocs
    withType<Javadoc> {
        options.encoding = "UTF-8"
        (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
        isFailOnError = false
    }

    // Copy version from project to `plugin.yml`, because gradle can't do
    // that automatically ¯\_(ツ)_/¯
    named<ProcessResources>("processResources") {
        filteringCharset = "UTF-8"
        duplicatesStrategy = DuplicatesStrategy.INCLUDE

        from(sourceSets.main.get().resources.srcDirs) {
            include("plugin.yml")
            expand("version" to project.version)
        }
    }

    runServer {
        minecraftVersion("26.1.2")
    }
}


