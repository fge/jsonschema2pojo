/**
 * Copyright © 2010-2013 Nokia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.googlecode.jsonschema2pojo.integration;

import static com.googlecode.jsonschema2pojo.integration.util.CodeGenerationHelper.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.junit.Test;

import com.googlecode.jsonschema2pojo.integration.util.TestableJsonschema2PojoMojo;
import com.googlecode.jsonschema2pojo.maven.Jsonschema2PojoMojo;

public class RegressionIT {

    @Test
    @SuppressWarnings("rawtypes")
    public void pathWithSpacesInTheNameDoesNotFail() throws ClassNotFoundException, MalformedURLException {

        File file = new File("src/test/resources/schema/regression/spaces in path.json");

        File sourcesDirectory = generate(file, "com.example", new HashMap<String, Object>());
        ClassLoader resultsClassLoader = compile(sourcesDirectory);

        Class generatedType = resultsClassLoader.loadClass("com.example.Spaces_in_path");
        assertThat(generatedType, is(notNullValue()));

    }

    public static File generate(final File schema, final String targetPackage, final Map<String, Object> configValues) {
        final File outputDirectory = createTemporaryOutputFolder();

        try {
            Jsonschema2PojoMojo pluginMojo = new TestableJsonschema2PojoMojo().configure(new HashMap<String, Object>() {
                {
                    put("sourceDirectory", schema);
                    put("outputDirectory", outputDirectory);
                    put("project", getMockProject());
                    put("targetPackage", targetPackage);
                    putAll(configValues);
                }
            });

            pluginMojo.execute();
        } catch (MojoExecutionException e) {
            throw new RuntimeException(e);
        } catch (DependencyResolutionRequiredException e) {
            throw new RuntimeException(e);
        }

        return outputDirectory;
    }

    private static MavenProject getMockProject() throws DependencyResolutionRequiredException {

        MavenProject project = mock(MavenProject.class);
        when(project.getCompileClasspathElements()).thenReturn(new ArrayList<String>());

        return project;
    }

}
