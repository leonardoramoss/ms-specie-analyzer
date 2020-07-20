package io.species.analyzer.domain.species.analyzer;

import java.util.Arrays;

class PrimateAnalyzerTest {

    private final PrimateAnalyzer analyzer = new PrimateAnalyzer();

    void teste() {

        final var counter = 150;

        for(int i = 0; i < counter; i++) {
            int n = 6;
            final StringBuilder stringBuilder = new StringBuilder();
            for(int j = 1; j <= n; j++) {
                stringBuilder.append(randomAlphaNumeric(n));
                if(j != n) {
                    stringBuilder.append("-");
                }
            }
            final var dna = stringBuilder.toString().split("-");
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
