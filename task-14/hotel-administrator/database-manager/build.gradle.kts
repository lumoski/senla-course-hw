plugins {
    id("java-library")
}

val hibernateVersion = "5.6.15.Final"

dependencies {
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation(project(":core"))
    implementation("com.mysql:mysql-connector-j:8.3.0")
    implementation("org.hibernate:hibernate-core:$hibernateVersion")
    api("org.hibernate:hibernate-core:$hibernateVersion")
}