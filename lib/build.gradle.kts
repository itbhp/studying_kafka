plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.springframework.kafka:spring-kafka:2.8.2")
    implementation("io.arrow-kt:arrow-core:1.0.1")

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("org.springframework.kafka:spring-kafka-test:2.8.2")
    testImplementation("org.testcontainers:kafka:1.16.3")
    testImplementation("org.testcontainers:junit-jupiter:1.16.3")

    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.0")
}
