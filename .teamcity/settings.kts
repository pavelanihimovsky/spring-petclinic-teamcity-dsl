import jetbrains.buildServer.configs.kotlin.v2018_2.*
import jetbrains.buildServer.configs.kotlin.v2018_2.buildFeatures.Swabra
import jetbrains.buildServer.configs.kotlin.v2018_2.buildFeatures.swabra
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.v2018_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2018_2.vcs.GitVcsRoot

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

version = "2018.2"

project {
    vcsRoot(PetclinicVcs)

    sequence {
        parallel {
            build(Test1) {
            }
            build(Test2) {
            }
        }
        build(Run) {  }
    }
}

object Run : BuildType({
    name = "Run"

    vcs {
        root(PetclinicVcs)
    }
    steps {
        maven {
            goals = "clean"
        }
    }
})

object Test1 : BuildType({
    name = "Test1"

    vcs {
        root(PetclinicVcs)
    }
    steps {
        maven {
            goals = "clean test"
        }
    }
})

object Test2 : BuildType({
    name = "Test2"

    vcs {
        root(PetclinicVcs)
    }
    steps {
        maven {
            goals = "clean test"
        }
    }
})

object Build : BuildType({
    name = "Build"
    artifactRules = "target/*jar"

    vcs {
        root(PetclinicVcs)
    }
    steps {
        maven {
            goals = "package"
        }
    }
    triggers {
        vcs {
            groupCheckinsByCommitter = true
        }
    }
})

object PetclinicVcs : GitVcsRoot({
    name = "PetclinicVcs"
    url = "https://github.com/spring-projects/spring-petclinic.git"
})

fun cleanFiles(buildType: BuildType): BuildType {
    buildType.features {
        swabra {
        }
    }
    return buildType
}

fun wrapWithFeature(buildType: BuildType, featureBlock: BuildFeatures.() -> Unit): BuildType {
    buildType.features {
        featureBlock()
    }
    return buildType
}