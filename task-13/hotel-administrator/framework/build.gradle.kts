plugins {
    id("java-library")
}

dependencies {
    api(project(":framework:di"))
    api(project(":framework:transaction"))
    api(project(":framework:configurator"))
    api(project(":framework:util"))
}

allprojects {
    dependencies {
        // Reflection
        implementation("org.reflections:reflections:0.10.2")
    }
}