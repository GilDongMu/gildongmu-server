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
    implementation("org.springframework.data:spring-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("com.h2database:h2")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation ("org.webjars:sockjs-client:1.5.1")
    implementation ("org.webjars:stomp-websocket:2.3.4")

    implementation(project(":gildongmu-common"))
    implementation(project(":gildongmu-domain"))
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
