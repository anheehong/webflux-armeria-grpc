import com.google.protobuf.gradle.*
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

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:3.0.1")

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
    implementation ("javax.annotation:javax.annotation-api:1.3.2")

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
        // protocol buffer를 컴파일하는 protoc의 스펙을 지정
        artifact = "com.google.protobuf:protoc:3.21.12"
    }
    plugins {
        // 컴파일 과정중 추가할 부분, 해당 프로젝트는 grpc 모델이 만들어여야하므로 아래의 설정이 있음.
        id("grpc"){
            artifact = "io.grpc:protoc-gen-grpc-java:1.51.0"
        }
    }
    generateProtoTasks{
        all().forEach { task ->
                task.generateDescriptorSet = true
                task.descriptorSetOptions.includeSourceInfo = true
                task.descriptorSetOptions.includeImports = true
                task.descriptorSetOptions.path = "${buildDir}/resources/main/META-INF/armeria/grpc/service-name.dsc"
                task.plugins{
                    id("grpc")
                }
            }
        }
}

sourceSets {
    main {
        java {
            srcDir("build/generated/source/proto/main/grpc")
            srcDir("build/generated/source/proto/main/java")
        }
    }
}