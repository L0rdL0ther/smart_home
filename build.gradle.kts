import org.w3c.dom.Element
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory


plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa") version "1.9.25"
}

group = "com.cri"
version = "0.0.1-SNAPSHOT"

val baseLibLatestVersion = getLatestVersion("wanim-library/com/wanim_ms/wanim-library")

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
    maven {
        url = uri("http://192.168.1.25:8232/repository/wanim-library/")
        isAllowInsecureProtocol = true
        credentials {
            username =  "admin"
            password = "QngrDP@A2ci5I^dzeL7aU\$I8g!Eg*694"
        }
    }

}

dependencies {

    implementation("com.wanim_ms:wanim-library:$baseLibLatestVersion")

    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")


    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    implementation("io.jsonwebtoken:jjwt-impl:0.12.6")
    implementation("io.jsonwebtoken:jjwt-jackson:0.12.6")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}


fun getLatestVersion(repositoryPath: String): String {
    val client = HttpClient.newHttpClient()
    val url = "http://192.168.1.25:8232/repository/$repositoryPath/maven-metadata.xml"
    val authHeader = getAuthHeader()

    val request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .header("Authorization", authHeader)
        .build()

    val response = client.send(request, HttpResponse.BodyHandlers.ofString())

    return if (response.statusCode() == 200) {
        val xmlContent = response.body()
        val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlContent.byteInputStream())
        getLatestVersionFromXml(document)
    } else {
        println("Request failed with status: ${response.statusCode()}")
        "Error"
    }
}

fun getLatestVersionFromXml(document: org.w3c.dom.Document): String {
    val metadataElement = document.getElementsByTagName("metadata").item(0) as Element
    val versioningElement = metadataElement.getElementsByTagName("versioning").item(0) as Element
    val latestElement = versioningElement.getElementsByTagName("latest").item(0) as Element
    return latestElement.textContent
}

fun getAuthHeader(): String {
    val usernameEnv = System.getenv("REPO_USERNAME") ?: "admin"
    val passwordEnv = System.getenv("REPO_PASSWORD") ?: "QngrDP@A2ci5I^dzeL7aU\$I8g!Eg*694"
    return "Basic " + Base64.getEncoder().encodeToString("$usernameEnv:$passwordEnv".toByteArray())
}
