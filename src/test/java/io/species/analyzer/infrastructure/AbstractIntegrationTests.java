package io.species.analyzer.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONValue;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.xml.sax.InputSource;

import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.dbunit.Assertion.assertEqualsByQuery;

@DirtiesContext
@SpringBootTest
@ActiveProfiles({"test"})
@AutoConfigureMockMvc
public abstract class AbstractIntegrationTests {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private IDatabaseConnection databaseConnection;

    protected String getJsonFileAsString(final String path) {
        final var resourceAsStream = this.getClass().getResourceAsStream(String.format("/%s", path));
        return JSONValue.parse(new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8)).toString();
    }

    protected String getMvcResultAsString(final MvcResult mvcResult) throws UnsupportedEncodingException {
        return mvcResult.getResponse().getContentAsString();
    }

    private IDatabaseConnection getDatabaseConnection()  {
        try {
            if(this.databaseConnection == null) {
                this.databaseConnection = new DatabaseConnection(Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection());
            }
            return this.databaseConnection;
        } catch (DatabaseUnitException | SQLException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    protected void verifyDatabase(final String fileName, final String tableName, final String ... ignoredColumns) throws Exception {
        final var selectQuery = "SELECT * FROM " + tableName;
        assertEqualsByQuery(getDataSet("/expected/datasets/" + fileName), getDatabaseConnection(), selectQuery, tableName, ignoredColumns);
    }

    private IDataSet getDataSet(final String dataset) throws DataSetException {
        try {
            final var source = new InputSource(getClass().getResourceAsStream(dataset));
            final var producer = new FlatXmlProducer(source, false, true);
            final var dataSet = new ReplacementDataSet(new FlatXmlDataSet(producer));

            dataSet.addReplacementObject("[NULL]", null);

            return dataSet;
        } catch (final DataSetException exception) {
            throw new DataSetException("Cannot read the dataset file " + dataset + "!", exception);
        }
    }

    protected DatabaseExpectationBuilder databaseExpectationBuilder() {
        return new DatabaseExpectationBuilder();
    }

    protected class DatabaseExpectationBuilder {

        private final Map<String, List<String>> mappedQueries = new HashMap<>();

        public DatabaseExpectationBuilder addExpectation(final String tableName) {
            return addExpectation(tableName, String.format("SELECT * FROM %s", tableName));
        }

        public DatabaseExpectationBuilder addExpectation(final String tableName, final String query) {
            if (mappedQueries.containsKey(tableName)) {
                mappedQueries.get(tableName).add(query);
            } else {
                mappedQueries.put(tableName, Collections.singletonList(query));
            }
            return this;
        }

        public void write(final String fileName) throws Exception {
            final var queryDataSet = new QueryDataSet(getDatabaseConnection());
            for (final Map.Entry<String, List<String>> entry : mappedQueries.entrySet()) {
                for (final String query : entry.getValue()) {
                    queryDataSet.addTable(entry.getKey(), query);
                }
            }
            FlatXmlDataSet.write(queryDataSet, new FileOutputStream(String.format("src/test/resources/%s", fileName)));
        }
    }
}
