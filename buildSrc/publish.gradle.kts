import com.jfrog.bintray.gradle.BintrayExtension.*
import groovy.util.Node
import org.gradle.jvm.tasks.Jar

val githubId = "StephaneBg/SimpleNumberPicker"
val repoWeb = "https://github.com/$githubId"
val repoVcs = "$repoWeb.git"
val tags = listOf("number-picker", "number", "picker", "hexadecimal")
val licenseId = "Apache-2.0"
val licenseName = "The Apache Software License, Version 2.0"
val licenseUrl = "http://www.apache.org/licenses/LICENSE-2.0.txt"
val releaseTag = "2.1.0"

val sourcesJar by tasks.creating(Jar::class) {
    dependsOn("classes")
    classifier = "sources"
    from(sourceSets["main"].allSource)
}

publishing {
    publications {
        create("MavenJava", MavenPublication::class.java) {
            from(components["java"])
            artifact(sourcesJar)
            pom.withXml {
                NodeScope(asNode()) {
                    "name" to project.name
                    "description" to project.description.toString()
                    "url" to repoWeb
                    "developers" {
                        "developer" {
                            "name" to "Stéphane Baiget"
                            "email" to "stephane@baiget.fr"
                            "organizationUrl" to "https://github.com/StephaneBg"
                        }
                    }
                    "issueManagement" {
                        "system" to "GitHub Issues"
                        "url" to "$repoWeb/issues"
                    }
                    "scm" {
                        "url" to repoWeb
                        "connection" to "scm:git:$repoVcs"
                        "developerConnection" to "scm:git:$repoVcs"
                        "tag" to releaseTag
                    }
                    "licenses" {
                        "license" {
                            "name" to licenseName
                            "url" to licenseUrl
                        }
                    }
                }
            }
        }
    }
}

bintray {
    user = project.properties["bintray.user"]?.toString()
    key = project.properties["bintray.key"]?.toString()
    setPublications("MavenJava")
    publish = true
    pkg(delegateClosureOf<PackageConfig> {
        repo = project.properties["bintray.repo"]?.toString() ?: "maven"
        name = project.name
        desc = description
        githubRepo = githubId
        githubReleaseNotesFile = "CHANGELOG.md"
        websiteUrl = repoWeb
        issueTrackerUrl = "$repoWeb/issues"
        vcsUrl = repoVcs
        setLicenses(licenseId)
        setLabels(*tags.toTypedArray())
        version(delegateClosureOf<VersionConfig> {
            name = project.version.toString()
            vcsTag = releaseTag
            mavenCentralSync(delegateClosureOf<MavenCentralSyncConfig> {
                sync = project.properties["sonatype.user"] != null
                user = project.properties["sonatype.user"]?.toString()
                password = project.properties["sonatype.password"]?.toString()
                close = "true"
            })
        })
    })
}

/**
 * Helper DSL to define Pom
 */
class NodeScope(private val node: Node, block: NodeScope.() -> Unit) {
    init {
        block()
    }

    infix fun String.to(value: String) {
        node.appendNode(this, value)
    }

    operator fun String.invoke(block: NodeScope.() -> Unit) {
        node.appendNode(this).apply { NodeScope(this, block) }
    }
}
