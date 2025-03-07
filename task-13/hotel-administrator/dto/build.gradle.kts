val mapstructVersion = "1.5.5.Final"

dependencies {
    implementation(project(":core"))
    implementation(project(":database-manager"))

    // Mapstruct
    implementation("org.mapstruct:mapstruct:$mapstructVersion")
    annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")
}