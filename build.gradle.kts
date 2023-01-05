import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.1"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"

    id("com.google.protobuf") version "0.8.16"
}

group = "com.webflux"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    //https://armeria.dev/tutorials/grpc/blog
    //https://github.com/google/protobuf-gradle-plugin
    implementation("com.linecorp.armeria:armeria-spring-boot2-webflux-starter")
    implementation("com.linecorp.armeria:armeria-grpc")
    implementation("com.google.protobuf:protobuf-java:3.21.12")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencyManagement {
    imports {
        mavenBom("com.linecorp.armeria:armeria-bom:1.21.0")
        mavenBom("io.netty:netty-bom:4.1.86.Final")
    }
}

protobuf {
    protoc{
        artifact = "com.google.protobuf:protoc:3.21.12"
    }
    generateProtoTasks{
        all().forEach() { task ->
                task.generateDescriptorSet = true
                task.descriptorSetOptions.includeSourceInfo = true
                task.descriptorSetOptions.includeImports = true
            }
        }
}