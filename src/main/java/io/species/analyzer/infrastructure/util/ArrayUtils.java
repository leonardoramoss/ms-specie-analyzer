package io.species.analyzer.infrastructure.util;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class ArrayUtils {

    private static final String DELIMITER = "-";

    private ArrayUtils() {}

    public static char[][] stringArrayToMultidimensionalChar(final String[] stringArray) {
        final int N = stringArray.length;
        final char[][] multidimensionalArray = new char[N][N];

        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                multidimensionalArray[i][j] = stringArray[i].toCharArray()[j];
            }
        }

        return multidimensionalArray;
    }

    public static String[] multidimensionalCharToStringArray(final char[][] multidimensionalChar) {
        final var builder = new StringBuilder();

        for(int i = 0; i < multidimensionalChar.length; i++) {
            builder.append(String.valueOf(multidimensionalChar[i]));
            if(i != multidimensionalChar.length - 1) {
                builder.append(DELIMITER);
            }
        }

        return builder.toString().split(DELIMITER);
    }

    public static String[] transposeStringArrayValues(final String[] stringArray) {
        final char[][] arrayToMultidimensionalChar = stringArrayToMultidimensionalChar(stringArray);
        final char[][] transposeRowToColumn = transposeRowToColumn(arrayToMultidimensionalChar);
        return multidimensionalCharToStringArray(transposeRowToColumn);
    }

    public static String[] reverseStringArrayValues(final String[] stringArray) {
        return Arrays.stream(stringArray)
                .map(string -> new StringBuilder(string).reverse().toString())
                .collect(Collectors.toList())
                .toArray(String[]::new);
    }

    public static char[][] transposeRowToColumn(final char[][] originalMultidimensionalChar) {

        final char[][] multidimensionalArray = new char[originalMultidimensionalChar.length][originalMultidimensionalChar[0].length];

        for (int i = 0; i < originalMultidimensionalChar[0].length; i++) {
            for (int j = 0; j < originalMultidimensionalChar.length; j++) {
                multidimensionalArray[i][j] = originalMultidimensionalChar[j][i];
            }
        }

        return multidimensionalArray;
    }

    public static String[] diagonalMultidimensionalCharToArrayString(final char[][] multidimensionalChar) {

        final var length = multidimensionalChar.length;
        final var diagonalLines = (length + length) - 1;
        final var midPoint = (diagonalLines / 2) + 1;
        final var output = new StringBuilder();

        final var itemsInDiagonal= new AtomicLong();

        for (int i = 1; i <= diagonalLines; i++) {

            final var items = new StringBuilder();

            int rowIndex;
            int columnIndex;

            if (i <= midPoint) {
                itemsInDiagonal.incrementAndGet();
                for (var j = 0; j < itemsInDiagonal.get(); j++) {
                    rowIndex = (i - j) - 1;
                    columnIndex = j;
                    items.append(multidimensionalChar[rowIndex][columnIndex]);
                }
            } else {
                itemsInDiagonal.decrementAndGet();
                for (var j = 0; j < itemsInDiagonal.get(); j++) {
                    rowIndex = (length - 1) - j;
                    columnIndex = (i - length) + j;
                    items.append(multidimensionalChar[rowIndex][columnIndex]);
                }
            }

            if (i != diagonalLines) {
                output.append(items).append(" ");
            }
        }

        return output.toString().split(" ");
    }
}
