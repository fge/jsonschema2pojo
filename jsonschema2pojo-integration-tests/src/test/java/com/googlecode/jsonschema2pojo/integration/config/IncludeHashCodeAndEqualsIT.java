/**
 * Copyright © 2010-2011 Nokia
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

package com.googlecode.jsonschema2pojo.integration.config;

import static com.googlecode.jsonschema2pojo.integration.util.CodeGenerationHelper.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.googlecode.jsonschema2pojo.Schema;

public class IncludeHashCodeAndEqualsIT {

    @Before
    public void clearSchemaCache() {
        Schema.clearCache();
    }

    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void beansIncludeHashCodeAndEqualsByDefault() throws ClassNotFoundException, SecurityException, NoSuchMethodException {

        ClassLoader resultsClassLoader = generateAndCompile("/schema/properties/primitiveProperties.json", "com.example");

        Class generatedType = resultsClassLoader.loadClass("com.example.PrimitiveProperties");

        // throws NoSuchMethodException if method is not found
        generatedType.getDeclaredMethod("equals", Object.class);
        generatedType.getDeclaredMethod("hashCode");

    }

    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void beansOmitHashCodeAndEqualsWhenConfigIsSet() throws ClassNotFoundException, SecurityException, NoSuchMethodException {
        ClassLoader resultsClassLoader = generateAndCompile("/schema/properties/primitiveProperties.json", "com.example", config("includeHashcodeAndEquals", false));

        Class generatedType = resultsClassLoader.loadClass("com.example.PrimitiveProperties");

        try {
            generatedType.getDeclaredMethod("equals", Object.class);
            fail(".equals method is present, it should have been omitted");
        } catch (NoSuchMethodException e) {
        }

        try {
            generatedType.getDeclaredMethod("hashCode");
            fail(".hashCode method is present, it should have been omitted");
        } catch (NoSuchMethodException e) {
        }
    }

}
