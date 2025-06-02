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
package uk.ac.ebi.embl.converter.gff3toff;

import java.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.embl.converter.fftogff3.FFtoGFF3ConversionError;
import uk.ac.ebi.embl.converter.gff3.GFF3Annotation;
import uk.ac.ebi.embl.converter.gff3.reader.GFF3FileReader;
import uk.ac.ebi.embl.converter.gff3.reader.GFF3ValidationError;
import uk.ac.ebi.embl.flatfile.writer.embl.EmblEntryWriter;

public class Gff3ToFFConverter {

    private static final Logger LOG = LoggerFactory.getLogger(Gff3ToFFConverter.class);

    public void convert(BufferedReader reader, BufferedWriter writer) throws FFtoGFF3ConversionError {
        try (GFF3FileReader gff3Reader = new GFF3FileReader(reader)) {
            GFF3Mapper mapper = new GFF3Mapper();
            gff3Reader.readHeader();
            GFF3Annotation annotation;
            while ((annotation = gff3Reader.readAnnotation()) != null) {
                LOG.info("Converting Gff3 entry: {}", getAccession(annotation));
                EmblEntryWriter entryWriter = new EmblEntryWriter(mapper.mapGFF3ToEntry(annotation));
                entryWriter.setShowAcStartLine(false);
                entryWriter.write(writer);
            }
        } catch (IOException e) {
            throw new FFtoGFF3ConversionError("IO Error during conversion", e);
        } catch (GFF3ValidationError e) {
            throw new FFtoGFF3ConversionError(
                    String.format("Validation Error on line %d: %s", e.getLine(), e.getMessage()), e);
        }
    }

    private String getAccession(GFF3Annotation gff3Annotation) {
        return gff3Annotation.getFeatures().stream().findFirst().get().getAccession();
    }
}
