plugins {
    id("java-library")
}

dependencies {
    api(project(":service"))
    api(project(":dto"))
    api(project(":database-manager"))
    api(project(":framework"))
}