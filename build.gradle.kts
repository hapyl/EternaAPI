plugins {
    `java-library`
    `maven-publish`

    id("io.papermc.paperweight.userdev") version "2.0.0-beta.21"
    id("maven-publish")
    id("xyz.jpenilla.run-paper") version "3.0.2"
    id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.3.1"
}

repositories {
    mavenCentral()

    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle("26.2.build.+")
}

group = "me.hapyl"
version = "6.3.1-SNAPSHOT"
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

    // Create `plugin.yml`
    bukkitPluginYaml {
        main = "me.hapyl.eterna.EternaPlugin"
        prefix = "EternaAPI"
        apiVersion = "26.2"
        authors = listOf("hapyl")
    }

    runServer {
        minecraftVersion("26.2")
    }
}




