import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.jetbrains.kotlin.plugin.jpa") version "1.3.21"
    id("org.springframework.boot") version "2.1.3.RELEASE"
    id("org.jetbrains.kotlin.jvm") version "1.3.21"
    id("org.jetbrains.kotlin.plugin.spring") version "1.3.21"
    id("io.spring.dependency-management") version "1.0.7.RELEASE"
}

apply(plugin = "kotlin-jpa")

group = "io.github.tschie.sparql"

repositories {
    mavenCentral()
    maven("http://www.hibernatespatial.org/repository")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.hibernate:hibernate-spatial:5.4.1.Final")

    implementation("org.postgresql:postgresql:42.2.5")
    implementation("net.postgis:postgis-jdbc:2.2.1")
    implementation("com.bedatadriven:jackson-datatype-jts:2.4")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<BootJar> {
    archiveFileName.set("sparql.jar")
}

tasks.withType<Wrapper> {
    gradleVersion = "5.4"
}
