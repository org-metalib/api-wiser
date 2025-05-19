package org.metalib.wiser.api.mvn.plugin;

import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.WithoutMojo;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the MyMojo class.
 * 
 * These tests verify that the touch goal works correctly.
 */
public class MyMojoTest {

    /**
     * The MojoRule is used to look up and configure Mojo instances for testing.
     */
    @Rule
    public MojoRule rule = new MojoRule() {
        @Override
        protected void before() {
            // Setup before each test
        }

        @Override
        protected void after() {
            // Cleanup after each test
        }
    };

    /**
     * Tests that the touch goal creates a touch.txt file in the output directory.
     * 
     * This test:
     * 1. Looks up a configured MyMojo instance
     * 2. Executes the mojo
     * 3. Verifies that the output directory and touch.txt file are created
     * 
     * @throws Exception if any error occurs during the test
     */
    @Test
    public void testSomething() throws Exception {
        // Get the test project POM
        File pom = new File("target/test-classes/project-to-test/");
        assertNotNull(pom);
        assertTrue(pom.exists());

        // Look up the Mojo
        MyMojo myMojo = (MyMojo) rule.lookupConfiguredMojo(pom, "touch");
        assertNotNull(myMojo);

        // Execute the Mojo
        myMojo.execute();

        // Verify the output directory exists
        File outputDirectory = (File) rule.getVariableValueFromObject(myMojo, "outputDirectory");
        assertNotNull(outputDirectory);
        assertTrue(outputDirectory.exists());

        // Verify the touch.txt file exists
        File touch = new File(outputDirectory, "touch.txt");
        assertTrue(touch.exists());
    }

    /**
     * A simple test that doesn't need the MojoRule.
     * 
     * This is an example of a test that doesn't require the Mojo to be instantiated.
     */
    @WithoutMojo
    @Test
    public void testSomethingWhichDoesNotNeedTheMojoAndProbablyShouldBeExtractedIntoANewClassOfItsOwn() {
        assertTrue(true);
    }

}
