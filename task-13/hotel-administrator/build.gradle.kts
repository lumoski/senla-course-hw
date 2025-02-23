allprojects {
    apply(plugin = "java")
    apply(plugin = "checkstyle")

    group = "com.hotel"
    version = "1.0.12"

    repositories {
        mavenCentral()
    }

    val lombokVersion = "1.18.36"
    val slf4jVersion = "2.0.9"
    val logbackVersion = "1.5.12"

    dependencies {
        // Lombok
        compileOnly("org.projectlombok:lombok:$lombokVersion")
        annotationProcessor("org.projectlombok:lombok:$lombokVersion")
        testCompileOnly("org.projectlombok:lombok:$lombokVersion")
        testAnnotationProcessor("org.projectlombok:lombok:$lombokVersion")

        // Logging
        implementation("org.slf4j:slf4j-api:$slf4jVersion")
        implementation("ch.qos.logback:logback-classic:$logbackVersion")

        // Test
        testCompileOnly("org.projectlombok:lombok:${lombokVersion}")
        testAnnotationProcessor("org.projectlombok:lombok:${lombokVersion}")
        testImplementation(platform("org.junit:junit-bom:5.10.0"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }

    tasks.test {
        useJUnitPlatform()
    }

    configure<CheckstyleExtension> {
        toolVersion = "10.12.5"
        configFile = rootDir.resolve("config/checkstyle/checkstyle.xml")
        isIgnoreFailures = false
        maxErrors = 0
        maxWarnings = 0
    }

    tasks.register("validateCodeStyle") {
        group = "verification"
        description = "Validates code style after compilation"
        dependsOn("checkstyleMain", "checkstyleTest")
    }

    tasks.named("check") {
        dependsOn("validateCodeStyle")
    }

    tasks.named("build") {
        dependsOn("check")
    }
}

plugins {
    id("application")
}

dependencies {
    implementation(project(":controller-console-ui"))
}

application {
    mainClass.set("com.hotel.Main")
}