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
package uk.ac.ebi.embl.converter.gff3;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class GFF3Annotation implements IGFF3Feature {
    GFF3Directives directives = new GFF3Directives();
    List<GFF3Feature> features = new ArrayList<>();

    private void writeFeature(Writer writer, GFF3Feature feature) throws IOException {
        writer.write(feature.getAccession());
        writer.write('\t' + feature.getSource());
        writer.write('\t' + feature.getName());
        writer.write("\t%d".formatted(feature.getStart()));
        writer.write("\t%d".formatted(feature.getEnd()));
        writer.write('\t' + feature.getScore());
        writer.write('\t' + feature.getStrand().toString());
        writer.write('\t' + feature.getPhase());
        writeAttributes(writer, feature);
        writer.write("\n");
    }

    private void writeAttributes(Writer writer, GFF3Feature feature) throws IOException {
        writer.write('\t');
        writer.write(feature.getAttributes().entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // Sort by key
                .map(entry -> {
                    String key = entry.getKey();
                    Object value = entry.getValue();

                    if (value instanceof List) {
                        List<String> values = (List<String>) value;
                        String joinedValue =
                                values.stream().map(GFF3Annotation::urlEncode).collect(Collectors.joining(","));
                        return urlEncode(key) + "=" + joinedValue;
                    } else {
                        return urlEncode(key) + "=" + urlEncode((String) value);
                    }
                })
                .collect(Collectors.joining(";", "", ";")));
    }

    private static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8").replace("+", " ");
        } catch (UnsupportedEncodingException e) {
            // We know for a fact that UTF-8 is supported. This branch won't be executed.
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeGFF3String(Writer writer) throws IOException {
        this.directives.writeGFF3String(writer);
        for (GFF3Feature feature : features) {
            writeFeature(writer, feature);
        }
        writer.write('\n');
    }

    public void addFeature(GFF3Feature feature) {
        features.add(feature);
    }
}
