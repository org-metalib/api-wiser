package org.metalib.wiser.api.mvn.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Goal which touches a timestamp file.
 * 
 * This is an example goal that demonstrates how to create a simple Maven plugin.
 * It creates a file called "touch.txt" in the project's build directory.
 */
@Mojo( name = "touch", defaultPhase = LifecyclePhase.PROCESS_SOURCES )
public class MyMojo extends AbstractMojo {
    /**
     * Location of the file.
     * 
     * This parameter specifies the directory where the touch.txt file will be created.
     * By default, it's set to the project's build directory.
     */
    @Parameter( defaultValue = "${project.build.directory}", property = "outputDir", required = true )
    private File outputDirectory;

    /**
     * Executes the goal.
     * 
     * This method creates a file called "touch.txt" in the output directory.
     * If the output directory doesn't exist, it will be created.
     * 
     * @throws MojoExecutionException if an error occurs while creating the file
     */
    public void execute() throws MojoExecutionException  {
        File f = outputDirectory;

        // Create the output directory if it doesn't exist
        if ( !f.exists() ) {
            f.mkdirs();
        }

        // Create the touch.txt file
        File touch = new File( f, "touch.txt" );

        FileWriter w = null;
        try {
            w = new FileWriter( touch );

            // Write content to the file
            w.write( "touch.txt" );
        } catch ( IOException e ) {
            throw new MojoExecutionException( "Error creating file " + touch, e );
        } finally {
            // Close the file writer
            if ( w != null ) {
                try {
                    w.close();
                } catch ( IOException e ) {
                    // ignore
                }
            }
        }
    }
}
