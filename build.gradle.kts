import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val dockerRepository = "artifactory.zoomint.com"
val dockerImagePrefix = "eleveo/wfo-analytics"
val imageName = "$dockerRepository/$dockerImagePrefix/${project.name}"

plugins {
    id("org.springframework.boot") version "2.5.5"
    kotlin("jvm") version "1.5.10"
    kotlin("plugin.spring") version "1.5.10"
    id("org.openapi.generator") version "5.1.1"
    id("com.bmuschko.docker-spring-boot-application") version "7.0.1"
    id("io.gitlab.arturbosch.detekt").version("1.17.1")
    id("com.avast.gradle.docker-compose") version "0.14.3"
    jacoco
    id("org.owasp.dependencycheck") version "6.3.2"
    id("org.unbroken-dome.helm") version "1.6.1" apply true
    id("org.unbroken-dome.helm-releases") version "1.6.1" apply true
    id("org.unbroken-dome.helm-publish") version "1.6.1" apply true
}

apply(plugin = "io.spring.dependency-management")

repositories {
    maven { url = uri("https://artifactory.zoomint.com/artifactory/zoom-ci-release") }
    maven { url = uri("https://artifactory.zoomint.com/artifactory/ci-libs-release") }
    maven { url = uri("https://artifactory.zoomint.com:80/artifactory/remote-repos") }
    maven { url = uri("https://artifactory.zoomint.com/artifactory/zoom-dev-release") }
    maven { url = uri("https://artifactory.zoomint.com/artifactory/zoom-dev-snapshot") }
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("io.r2dbc:r2dbc-postgresql")
    implementation("io.r2dbc:r2dbc-pool")
    implementation("org.flywaydb:flyway-core")
    implementation("org.postgresql:postgresql") // Needed for flyway
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("javax.validation:validation-api:2.0.1.Final")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework:spring-jdbc")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("com.zoomint.keycloak:keycloak-client-orchestrator:0.3.4")
    implementation("com.zoomint.keycloak:keycloak-api-provider-client:4.3.1")
    implementation("com.zoomint.keycloak:keycloak-api-provider-dto:4.3.1")
    implementation("com.zoomint.keycloak:users-api-provider-client:1.7.2")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.rest-assured:rest-assured")
    testImplementation("com.zoomint.keycloak:keycloak-helper-lib:2.4.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")
    testImplementation("io.r2dbc:r2dbc-h2")
    testImplementation("com.h2database:h2")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		jvmTarget = "11"
	}
}

tasks.withType<Test>(){
    useJUnitPlatform()
    finalizedBy(tasks.getByPath("composeDown"))
    finalizedBy(tasks.jacocoTestReport)
    finalizedBy(tasks.jacocoTestCoverageVerification)
}

tasks.getByPath("test").dependsOn(tasks.getByPath("composeUp"))

val apiCodeGeneratedDir = "${project.buildDir}/generated"

tasks.register<Copy>("copyOpenApiGeneratorIgnore") {
    from(file("${project.rootDir}/src/main/resources/.openapi-generator-ignore"))
    into(file(apiCodeGeneratedDir))
}

openApiGenerate {
    generatorName.set("kotlin-spring")
    configOptions.apply {
        put("reactive", "true")
        put("delegatePattern", "true")
    }
    inputSpec.set("${project.rootDir}/src/main/resources/openapi.yaml")
    outputDir.set(apiCodeGeneratedDir)
    apiPackage.set("com.zoomint.wfo.analytics.core.api.generated")
    modelPackage.set("com.zoomint.wfo.analytics.core.api.generated.model")
    invokerPackage.set("com.zoomint.wfo.analytics.core.api.generated.invoker")
    generateApiTests.set(false)
    generateModelTests.set(false)
    generateApiDocumentation.set(true)
}

kotlin {
    sourceSets["main"].apply {
        kotlin.srcDir("$apiCodeGeneratedDir/src/main/kotlin")
    }
}

tasks.getByPath(":openApiGenerate").dependsOn(tasks.getByPath("copyOpenApiGeneratorIgnore"))
tasks.getByPath("compileKotlin").dependsOn(tasks.getByPath("openApiGenerate"))

detekt {
    input = files("src/main/kotlin", "src/test/kotlin")
    autoCorrect = true
}

dependencyCheck {
    cve(closureOf<org.owasp.dependencycheck.gradle.extension.CveExtension> {
        urlModified = "http://repository.zoomint.com/repositories/nist/nvdcve-1.1-modified.json.gz"
        urlBase = "http://repository.zoomint.com/repositories/nist/nvdcve-1.1-%d.json.gz"
    })
    //TODO enable when keycloak vulnerabilities are resolved
//    failBuildOnCVSS = if (project.hasProperty("suppressOwasp")) 11F else 0F
}

docker {
    val dockerRepository = "artifactory.zoomint.com"

    springBootApplication {
        baseImage.set("artifactory.zoomint.com/eleveo/base-images/centos8java11:2.2.9")
        images.add("$imageName:${project.version}")
        images.add("$imageName:latest")
    }

    registryCredentials {
        url.set(dockerRepository)
        username.set(System.getProperty("username"))
        password.set(System.getProperty("password"))
    }
}
tasks.getByPath("build").dependsOn(tasks.getByPath("dockerBuildImage"))


helm {
    charts {
        create("core") {
            chartName.set("wfo-analytics-core")
            chartVersion.set("${project.version}")
            sourceDir.set(file("helm"))
            filtering {
                values.put("dockerTag", project.version)
                values.put("dockerRepository", "")
                values.put("imageName", "$dockerImagePrefix/${project.name}")
            }
        }
    }

    publishing {
        repositories {
            create<org.unbrokendome.gradle.plugins.helm.publishing.dsl.CustomHelmPublishingRepository>("jfrogArtifactory") {
                url.set(uri("http://artifactory.zoomint.com/"))
                uploadMethod.set("PUT")
                // We need to specify the file name in the url due to
                // inability of JFrog to figure out the file name from
                // the file which is being uploaded
                uploadPath.set("artifactory/helmcharts/wfo-analytics/${project.name}-${project.version}.tgz")
                credentials {
                    username.set(System.getProperty("username"))
                    password.set(System.getProperty("password"))
                }
            }
        }
    }
}

tasks.register("removeSnapshotFromVersion") {
    val versionWithoutSnapshot = project.version.toString().replace("-SNAPSHOT", "")
    doLast {
        val p = file("gradle.properties").readText()
            .replace("version=${project.version}", "version=$versionWithoutSnapshot")
        file("gradle.properties").writeText(p, Charsets.UTF_8)
        project.version = versionWithoutSnapshot
    }
}

tasks.register("increaseVersionNumberAndAddSnapshot") {
    val semanticVersion = project.version.toString().split(".")
    val patchAndSuffix = semanticVersion[2].split("-")
    val patch = patchAndSuffix[0].toInt()
    val increasedVersion = "${semanticVersion[0]}.${semanticVersion[1]}.${patch + 1}-SNAPSHOT"
    doLast {
        val p = file("gradle.properties").readText().replace("version=${project.version}", "version=$increasedVersion")
        file("gradle.properties").writeText(p, Charsets.UTF_8)
        project.version = increasedVersion
    }
}

dockerCompose {
    useComposeFiles = listOf("docker-compose.yaml")
}

jacoco {
    toolVersion = "0.8.7"
}
tasks.withType<JacocoCoverageVerification> {
    violationRules {
        rule {
            element = "BUNDLE"
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = 0.9.toBigDecimal()
            }
        }
    }
    afterEvaluate {
        classDirectories.setFrom(files(classDirectories.files.map {
            fileTree(it).apply {
                exclude("com/zoomint/**/generated/**")
            }
        }))
    }
    dependsOn(tasks.jacocoTestReport)
}





//-----------------------------------------------------------------------------------

tasks.register("printSomething") {
    doLast {
        println("Welcome to gradle world!")
    }
}
