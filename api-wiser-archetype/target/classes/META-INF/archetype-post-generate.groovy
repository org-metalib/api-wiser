
println "Executing the archetype-post-generate.groovy script...";

def outputDir = new File(request.getOutputDirectory(), request.artifactId)

def apiDir = new File(outputDir, '/api')
def apiFile0 = new File( apiDir, '_artifactId_.yaml' )
def apiFile1 = new File( apiDir, request.artifactId + '.yaml' )
if (apiFile1.exists()) {
    apiFile0.delete()
    println "Api $apiFile1 found."
} else if (apiFile0.exists()) {
    apiFile0.renameTo(apiFile1)
} else {
    throw new Exception("Api Start Template Not found.")
}

def proc = new ProcessBuilder(["mvn", "wrapper:wrapper", "initialize"])
        .directory(outputDir)
        .redirectErrorStream(true)
        .start()
proc.inputStream.eachLine {println it}
proc.waitForOrKill(10000)
if( proc.exitValue() != 0){
    String errorMsg = "ERROR during the maven execution."
    println "->  $errorMsg"
    throw new Exception("$errorMsg")
} else {
    println "-> Finished maven execution"
}

