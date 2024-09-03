plugins {
	java
	id("org.springframework.boot") version "3.2.4"
	id("io.spring.dependency-management") version "1.1.4"
}

version = "1.0"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-jetty")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.postgresql:postgresql")
	modules {
		module("org.springframework.boot:spring-boot-starter-tomcat") {
			replacedBy("org.springframework.boot:spring-boot-starter-jetty")
		}
	}
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation(platform("software.amazon.awssdk:bom:2.25.40"))
	implementation("software.amazon.awssdk:s3")
	implementation("org.webjars:bootstrap:5.3.3")
	implementation("org.webjars:jquery:3.7.1")
	implementation("org.webjars:webjars-locator-core")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
