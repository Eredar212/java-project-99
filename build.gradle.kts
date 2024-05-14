import com.adarshr.gradle.testlogger.theme.ThemeType
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
	application
	id("org.springframework.boot") version "3.1.4"
	id("io.spring.dependency-management") version "1.1.3"
	id("com.github.ben-manes.versions") version "0.49.0"
	id("io.freefair.lombok") version "8.6"
	jacoco
	id("com.adarshr.test-logger") version "3.2.0"
	checkstyle
}

group = "hexlet.code"

version = "1.0-SNAPSHOT"

application { mainClass.set("hexlet.code.AppApplication") }

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.mapstruct:mapstruct:1.5.5.Final")
	implementation("org.springframework.boot:spring-boot-starter-security")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.springframework.security:spring-security-test")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
	runtimeOnly("com.h2database:h2")
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-devtools")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("net.datafaker:datafaker:2.0.2")
	implementation("org.instancio:instancio-junit:3.3.1")
	implementation("org.openapitools:jackson-databind-nullable:0.2.6")

	implementation("org.apache.maven.reporting:maven-reporting-api:4.0.0-M11")
	implementation("org.jacoco:jacoco-maven-plugin:0.8.12")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation(platform("org.junit:junit-bom:5.10.0"))
	testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
	testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.2")

	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	testImplementation("org.springframework.security:spring-security-test")
}

tasks.test {
	useJUnitPlatform()
	testLogging {
		exceptionFormat = TestExceptionFormat.FULL
		events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
		showStandardStreams = true
	}
	finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport  {
	dependsOn (tasks.test) // tests are required to run before generating the report
			reports {
				xml.required = true
				html.required = true
			}
}
testlogger {
	theme = ThemeType.STANDARD
	showExceptions = true
	showStackTraces = true
	showFullStackTraces = false
	showCauses = true
	slowThreshold = 2000
	showSummary = true
	showSimpleNames = false
	showPassed = true
	showSkipped = true
	showFailed = true
	showOnlySlow = false
	showStandardStreams = false
	showPassedStandardStreams = true
	showSkippedStandardStreams = true
	showFailedStandardStreams = true
	logLevel = LogLevel.LIFECYCLE
}
