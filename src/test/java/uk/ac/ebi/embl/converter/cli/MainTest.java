/*
 * Copyright 2025 EMBL - European Bioinformatics Institute
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package uk.ac.ebi.embl.converter.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

public class MainTest {
    @Test
    public void testValidateFileType() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CommandConversion command = new CommandConversion();
        Method method = CommandConversion.class.getDeclaredMethod(
                "validateFileType", CommandConversion.FileFormat.class, Path.class, String.class);
        method.setAccessible(true);

        Object validate = null;

        validate = method.invoke(command, CommandConversion.FileFormat.embl, Path.of("foo"), "-f");
        assertEquals(CommandConversion.FileFormat.embl, validate, "If format is provided returns the same format");

        validate = method.invoke(command, CommandConversion.FileFormat.gff3, Path.of("foo"), "-f");
        assertEquals(CommandConversion.FileFormat.gff3, validate, "If format is provided returns the same format");

        validate = method.invoke(command, null, Path.of("foo.embl"), "-f");
        assertEquals(CommandConversion.FileFormat.embl, validate, "If format in path extension, use it");

        validate = method.invoke(command, null, Path.of("foo.gff3"), "-f");
        assertEquals(CommandConversion.FileFormat.gff3, validate, "If format in path extension, use it");

        try {
            validate = method.invoke(command, null, Path.of("foo.embl"), "-f");
        } catch (InvocationTargetException e) {
            assertTrue(
                    e.getTargetException().getMessage().contains("-f"),
                    "If no format matches extension, show error with options");
        }

        try {
            validate = method.invoke(command, null, Path.of("foo.embl"), "-t");
        } catch (InvocationTargetException e) {
            assertTrue(
                    e.getTargetException().getMessage().contains("-t"),
                    "If no format matches extension, show error with options");
        }
    }
}
