plugins {
	java
	id("org.springframework.boot") version "3.2.3"
	id("io.spring.dependency-management") version "1.1.4"
	jacoco
}

group = "codeit"
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

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.springframework.boot:spring-boot-starter-web")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	implementation ("io.jsonwebtoken:jjwt-api:0.12.5")
	runtimeOnly ("io.jsonwebtoken:jjwt-impl:0.12.5")
	runtimeOnly ("io.jsonwebtoken:jjwt-jackson:0.12.5")
	implementation("org.hibernate:hibernate-validator:8.0.1.Final")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	implementation(project(":gildongmu-domain"))
}


tasks.test {
	useJUnitPlatform()
}

tasks.register("prepareKotlinBuildScriptModel") {}
