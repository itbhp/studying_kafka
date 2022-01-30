plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.10"

    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.apache.kafka:kafka-clients:3.1.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}
