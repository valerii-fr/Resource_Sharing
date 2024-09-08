pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Resource Sharing"
include(":app")
include(":component")
include(":feature")
include(":component:network")
include(":feature:network_map")
include(":component:network:discovery")
include(":common_ui")
include(":core")
include(":component:services")
include(":component:settings")
include(":component:common")
include(":component:network:service_manager")
include(":component:network:publish")
include(":feature:homescreen")
include(":component:network:server_provider")
include(":component:network:client_provider")
include(":component:services:presentation")
include(":component:services:core")
