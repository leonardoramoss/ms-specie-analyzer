package io.species.analyzer.configuration.fixtures;

public interface DatabaseAssertion {

    void verifyDatabase(final String fileName, final String tableName, final String query, final String ... ignoredColumns);
}
