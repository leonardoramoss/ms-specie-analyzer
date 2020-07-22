package io.species.analyzer.domain.species.analyzer;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class PrimateAnalyzerTest {

    private final PrimateAnalyzer analyzer = new PrimateAnalyzer();
    private static final String DELIMITER = "-";

    @Test
    @Disabled
    void payloadDNAGenerator() {

        final var counter = 1_000_000;

        for(int i = 0; i < counter; i++) {

            final int N = 12;

            final var stringBuilder = new StringBuilder();

            for(int j = 1; j <= N; j++) {

                stringBuilder.append(randomAlphaNumeric(N));

                if(j != N) {
                    stringBuilder.append(DELIMITER);
                }
            }

            final var dna = stringBuilder.toString().split(DELIMITER);
            final var simian = analyzer.isSimian(dna);
            System.out.println(String.format("%s - isSimian: %s", Arrays.toString(dna), simian));
        }
    }

    static String randomAlphaNumeric(int count) {
        final var ALPHA_NUMERIC_STRING = "ACTG";
        final var builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
}
