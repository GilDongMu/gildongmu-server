plugins {
    java
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    jacoco
}

group = "chat"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

jacoco {
    toolVersion = "0.8.9"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.data:spring-data-mongodb")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        html.required.set(true)
    }
    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) {
                setIncludes(
                    listOf(
                        "codeit/**/controller/*",
                        "codeit/**/service/*",
                        "codeit/**/util/*",
                        "codeit/**/interceptor/*"
                    )
                )
            }
        })
    )
    finalizedBy(tasks.jacocoTestCoverageVerification)
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.jacocoTestReport)
    violationRules {
        rule {
            isEnabled = true
            element = "CLASS"
            includes = listOf(
                "codeit.**.controller.*",
                "codeit.**.service.*",
                "codeit.**.util.*",
                "codeit.**.interceptor.*"
            )

            limit {
                minimum = "0.50".toBigDecimal()
                counter = "LINE"
                value = "COVEREDRATIO"
            }
        }
    }
    finalizedBy(tasks.bootBuildImage)
}

tasks.bootBuildImage {
    dependsOn(tasks.jacocoTestCoverageVerification)
    builder.set("paketobuildpacks/builder-jammy-base:latest")
}

tasks.register("prepareKotlinBuildScriptModel") {}
