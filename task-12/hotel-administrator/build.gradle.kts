allprojects {
    apply(plugin = "java")

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
}

plugins {
    id("java")
    id("application")
}

dependencies {
    implementation(project(":hotel-controller"))
    implementation(project(":hotel-framework"))
}

application {
    mainClass.set("com.hotel.Main")
}