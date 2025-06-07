import java.nio.file.Files
import java.util.logging.FileHandler

println "Executing the archetype-post-generate.groovy script...";

def outputDir = new File(request.getOutputDirectory(), request.artifactId)

def apiDir = new File(outputDir, '/api')
def apiOriginFile = new File( System.getProperty('user.dir'), request.artifactId)
if (!apiOriginFile.exists()) {
    apiOriginFile = new File( System.getProperty('user.dir'), '/api')
}

def apiTemplateFile = new File( apiDir, '_artifactId_.yaml' )
def apiTargetFile = new File( apiDir, request.artifactId + '.yaml' )

def openapi = System.getProperty('openapi')
def openapiFile = new File(openapi)

if (apiTargetFile.exists()) {
    apiTemplateFile.delete()
    println "Api $apiTargetFile found."
} else if (openapiFile.exists()) {
    Files.copy(openapiFile.toPath(), apiTargetFile.toPath())
    apiTemplateFile.delete()
    println "openapi ($openapi) found."
} else if (apiOriginFile.exists()) {
    Files.copy(apiOriginFile.toPath(), apiTargetFile.toPath())
    apiTemplateFile.delete()
    println "original ($apiOriginFile) found."
} else if (apiTemplateFile.exists()) {
    apiTemplateFile.renameTo(apiTargetFile)
    println "Adding api file from a template ($apiTemplateFile)."
}

def proc = new ProcessBuilder(["mvn", "wrapper:wrapper", "initialize", "initialize"])
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

