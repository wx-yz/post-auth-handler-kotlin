plugins {
    id("org.jetbrains.kotlin.jvm") version "2.2.0-RC2"
}

group = "org.wso2.carbon.identity.post.authn.handler"
version = "4.6.4-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.wso2.org/nexus/content/groups/wso2-public/")
    }
}

dependencies {
    implementation(kotlin("osgi-bundle"))
    implementation("org.wso2.carbon.identity.framework:org.wso2.carbon.identity.application.authentication.framework:5.25.0")
    implementation("org.apache.felix:org.apache.felix.scr.ds-annotations:1.2.10")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    compileOnly("com.ibm.icu:icu4j:70.1")
}

sourceSets {
    main {
        kotlin {
            srcDir("src/kotlin")
        }
    }
}

tasks.named<Jar>("jar") {
    manifest {
        attributes(
            "Manifest-Version" to "1.0",
            "Bundle-Description" to "Custom Post Auth Handler",
            "Bundle-DocURL" to "http://www.wso2.org",
            "Bundle-License" to "http://www.apache.org/licenses/LICENSE-2.0",
            "Bundle-ManifestVersion" to "2",
            "DynamicImport-Package" to "*",
            "Bundle-Name" to "org.wso2.carbon.identity.post.authn.handler.disclaimer",
            "Bundle-SymbolicName" to "org.wso2.carbon.identity.post.authn.handler.disclaimer",
            "Bundle-Vendor" to "WSO2",
            "Bundle-Version" to "4.6.4.SNAPSHOT",
            "Export-Package" to """org.wso2.carbon.identity.post.authn.handler.disclaimer;version="4.6.4.SNAPSHOT";uses:="javax.servlet.http,org.wso2.carbon.identity.application.authentication.framework.context,org.wso2.carbon.identity.application.authentication.framework.exception,org.wso2.carbon.identity.application.authentication.framework.handler.request""",
            "Implementation-Title" to "org.wso2.carbon.identity.post.authn.handler.disclaimer",
            "Implementation-Vendor" to "WSO2",
            "Implementation-Vendor-Id" to "org.wso2.samples.custom.post.auth.handler",
            "Implementation-Version" to "4.6.4.SNAPSHOT",
            "Import-Package" to """javax.servlet.http;version="[2.6,3)",org.apache.commons.logging;version="[1.2,2)",org.osgi.framework;version="[1.9,2)",org.osgi.service.component;version="[1.2.0,2.0.0)",org.wso2.carbon.identity.application.authentication.framework.config;version="[7.0,8)",org.wso2.carbon.identity.application.authentication.framework.config.model;version="[7.0,8)",org.wso2.carbon.identity.application.authentication.framework.context;version="[7.0,8)",org.wso2.carbon.identity.application.authentication.framework.exception;version="[7.0,8)",org.wso2.carbon.identity.application.authentication.framework.handler.request;version="[7.0,8)",org.wso2.carbon.identity.application.authentication.framework.model;version="[7.0,8)",org.wso2.carbon.identity.core.util;version="[7.0,8)"""",
            "Service-Component" to "OSGI-INF/component.xml,OSGI-INF/org.wso2.carbon.identity.post.authn.handler.disclaimer.internal.DisclaimerPostAuthnHandlerServiceComponent.xml",
            "Specification-Title" to "org.wso2.carbon.identity.post.authn.handler.disclaimer",
            "Specification-Vendor" to "WSO2",
            "Specification-Version" to "4.6.4-SNAPSHOT",
            "Tool" to "Bnd-2.1.0.20130426-122213"
        )
    }
    from("src/resources/OSGI-INF") {
        into("OSGI-INF")
    }
} 