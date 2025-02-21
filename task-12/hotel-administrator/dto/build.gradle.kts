val mapstructVersion = "1.5.5.Final"

dependencies {
    implementation(project(":core"))

    // Mapstruct
    implementation("org.mapstruct:mapstruct:$mapstructVersion")
    annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")
}