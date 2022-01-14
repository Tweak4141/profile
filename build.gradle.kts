import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.32"
    application
    kotlin("plugin.serialization") version "1.5.20"
}

group = "me.abhijeet"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io/")
}

dependencies {
    // Server
    runtimeOnly("org.jetbrains.kotlin:kotlin-scripting-jsr223:1.4.10")
    implementation("io.ktor:ktor-server-core:1.6.3")
    implementation("io.ktor:ktor-server-netty:1.6.3")
    // Serialisation
    implementation("io.ktor:ktor-serialization:1.6.3")
    // ktor client
    implementation("io.ktor:ktor-client-core:1.6.3")
    implementation("io.ktor:ktor-client-cio:1.6.3")
    // HTML templating
    implementation("io.ktor:ktor-freemarker:1.6.3")
    // Logging for Ktor
    implementation("ch.qos.logback:logback-classic:1.2.5")

    // Discord API
    implementation("com.discord4j:discord4j-core:3.1.7")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.5.1")

    // Database
    implementation("org.postgresql:postgresql:42.2.23")
    implementation("com.zaxxer:HikariCP:5.0.0")
    implementation("org.flywaydb:flyway-core:7.14.1")

    // DI
    implementation("io.insert-koin:koin-ktor:3.1.2")

    // Image processing
    implementation("org.imgscalr:imgscalr-lib:4.2")

    // Testing
    testImplementation("io.ktor:ktor-server-test-host:1.6.3")
    testImplementation("io.insert-koin:koin-test:3.1.2")
    testImplementation("io.insert-koin:koin-test-junit4:3.1.2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("ApplicationKt")
}

tasks.create("stage") {
    dependsOn("installDist")
}
