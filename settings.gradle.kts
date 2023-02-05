dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}
rootProject.name = "ImageSearchApp"
include (":app")
include (":appcore:core")
include (":appcore:themes")
include(":appcore:preference")
include(":appcore:testutils")

include(":features:settings")
include(":features:imagesearch")
include(":features:imagedetails")
