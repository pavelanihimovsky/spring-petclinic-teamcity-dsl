package _Root

import jetbrains.buildServer.configs.kotlin.v2018_2.Project

object Root : Project({
    description = "Contains all other projects"

    subProject(Tests1)
})

object Tests1 : Project({

})