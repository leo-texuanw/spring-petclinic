import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2020.1"

project {
    vcsRoot(PetclinicVcs)
    buildType(wrapWithFeature(Build){
        swabra {}
    })
}

object Build : BuildType({
    id("Build")
    name = "Build"
    // artifactRules

    vcs {
        root(PetclinicVcs)
    }

    steps {
        maven {
            //goals = "clean test"
            //runnerArgs = "-Dmaven.test.failure.ignore=true"
            goals = "clean package"

            // Other options
            dockerImage = "maven:3.6.0-jdk.8"
            jvmArgs = "-Xmx512"
        }
    }

    triggers {
        vcs {

        }
    }
})

object PetclinicVcs : GitVcsRoot({
    name = "PetclinicVcs"
    url = "https://github.com/leo-texuanw/spring-petclinic.git"
    branch = "+:refs/heads/main"  // default
    authMethod = password {
        userName = "leo-texuanw"
        password = "credentialsJSON:a0157873-1240-4b28-97b7-cf4e8470c58f"
    }
})

fun wrapWithFeature(buildType: BuildType, featureBlock: BuildFeatures.() -> Unit): BuildType {
    buildType.features {
        featureBlock()
    }
    return buildType
}
