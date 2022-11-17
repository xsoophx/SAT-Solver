plugins {
    kotlin("jvm") version "1.6.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

object Version {
    const val JUNIT = "5.9.1"
    const val ASSERTK = "0.25"
    const val LOGBACK = "1.2.3"
    const val LOGGING = "3.0.2"
    const val SLF4J = "2.0.3"

}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test-junit5"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:${Version.JUNIT}")
    testImplementation("org.junit.jupiter:junit-jupiter-params:${Version.JUNIT}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${Version.JUNIT}")

    implementation("com.willowtreeapps.assertk:assertk:${Version.ASSERTK}")

    implementation("io.github.microutils:kotlin-logging-jvm:${Version.LOGGING}")
    runtimeOnly("ch.qos.logback:logback-classic:${Version.LOGBACK}")
    implementation("org.slf4j:slf4j-simple:${Version.SLF4J}")


}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
}