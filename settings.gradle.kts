rootProject.name = "PlotSquared"

include("Core", "Bukkit")

project(":Core").name = "PlotSquared-Core"
project(":Bukkit").name = "PlotSquared-Bukkit"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
