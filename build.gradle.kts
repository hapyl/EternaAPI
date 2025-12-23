plugins {
    `java-library`
    `maven-publish`

    id("io.papermc.paperweight.userdev") version "2.0.0-beta.19"
    id("maven-publish")
}

repositories {
    mavenCentral()

    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle("1.21.11-R0.1-SNAPSHOT")
}

group = "me.hapyl"
version = "5.2.2-SNAPSHOT"
description = "EternaAPI"
java.sourceCompatibility = JavaVersion.VERSION_21

// Attach sources and javadocs
java {
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
}


