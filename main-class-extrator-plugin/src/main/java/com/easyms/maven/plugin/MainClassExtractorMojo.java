package com.easyms.maven.plugin;

import com.google.cloud.tools.jib.api.MainClassFinder;
import com.google.cloud.tools.jib.filesystem.DirectoryWalker;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;

@Mojo(name = "sayhi")
public class MainClassExtractorMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    public void execute() throws MojoExecutionException {
        try {
            MainClassFinder.Result mainClassFinderResult =
                    MainClassFinder.find(Collections.unmodifiableList(new DirectoryWalker(Paths.get(project.getBuild().getOutputDirectory())).walk()), (myClass) -> {
                    });

            if (mainClassFinderResult.getType() != MainClassFinder.Result.Type.MAIN_CLASS_FOUND) {
                throw new MojoExecutionException("Unable to find className. Returned mainClassFinderResult type is " + mainClassFinderResult.getType());
            }

            project.getProperties().setProperty("mainClassName", mainClassFinderResult.getFoundMainClass());

            getLog().info("mainClassName is " + project.getProperties().getProperty("mainClassName"));
        } catch (IOException e) {
            throw new MojoExecutionException("Exception while extracting main class", e);
        }
    }
}



