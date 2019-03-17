
task("copyWebBundleToCoreResources", Copy::class) {
   val inputDirectory = "${project("web").buildDir}/bundle"
   println("input directory: $inputDirectory")
   val coreProject = project("plugin")
   val outputDirectory = "${coreProject.projectDir}/src/main/resources/web"


   println("output directory: $outputDirectory")

   from(file(inputDirectory))
   into(file(outputDirectory))
}

afterEvaluate {

   tasks["copyWebBundleToCoreResources"].dependsOn("web:webpack-bundle")
//   project("plugin").tasks["assemble"].dependsOn("copyWebBundleToCoreResources")
}