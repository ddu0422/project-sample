import org.jetbrains.kotlin.de.undercouch.gradle.tasks.download.Download
import java.io.ByteArrayOutputStream

plugins {
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
    id("com.google.cloud.tools.jib") version "3.4.3"
    kotlin("plugin.jpa") version "1.9.24"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
}

group = "per.study"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

jib {
    val env = project.properties["env"] as String? ?: "prod"
    val service = "test"

    from {
        image = "amazoncorretto:21"
    }
    to {
        image = "767397978317.dkr.ecr.ap-northeast-2.amazonaws.com/$service"
        project.afterEvaluate {
            tags = setOf("latest", "$env-${getGitHash()}")
        }
    }
    container {
        jvmFlags = listOf(
            "-javaagent:agents/dd-agent.jar"
        )
    }
}

fun getGitHash(): String {
    val stdOut = ByteArrayOutputStream()
    exec {
        commandLine("git", "rev-parse", "--short", "HEAD")
        standardOutput = stdOut
    }
    return stdOut.toString().trim()
}

tasks.register<Download>("downloadAgent") {
    src("https://project-sample-test.s3.ap-northeast-2.amazonaws.com/dd-java-agent.jar")
    dest("src/main/jib/agents/dd-java-agent.jar")
}

tasks.jib {
    dependsOn("downloadAgent")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
