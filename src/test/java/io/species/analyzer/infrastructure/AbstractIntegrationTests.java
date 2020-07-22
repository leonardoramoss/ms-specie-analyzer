package io.species.analyzer.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
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
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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

    protected String getMvcResultAsString(final MvcResult mvcResult) {
        try {
            return mvcResult.getResponse().getContentAsString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    protected MvcResult performPostWithPayloadAndExpect(final String endpoint, final String jsonPayload, final ResultMatcher expected) {
        try {
            return mockMvc.perform(
                    post(endpoint)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonPayload))
                    .andExpect(expected)
                    .andReturn();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    protected MvcResult performGetAndExpect(final String endpoint, final ResultMatcher expected) {
        try {
            return mockMvc.perform(get(endpoint)).andExpect(expected).andReturn();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
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

    protected JsonNode getJsonNodeFromJsonFile(final String path) {
        return getJsonNodeFromString(getJsonFileAsString(path));
    }

    protected JsonNode getJsonNodeFromString(final String string)  {
        try {
            return objectMapper.readTree(string);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected void verifyDatabase(final String fileName, final String tableName, final String ... ignoredColumns) {
        final var selectQuery = "SELECT * FROM " + tableName;
        verifyDatabaseWithQuery(fileName, tableName, selectQuery, ignoredColumns);
    }

    protected void verifyDatabaseWithQuery(final String fileName, final String tableName, final String query, final String ... ignoredColumns) {
        try {
            assertEqualsByQuery(getDataSet("/expected/datasets/" + fileName), getDatabaseConnection(), query, tableName, ignoredColumns);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private IDataSet getDataSet(final String dataset)  {
        try {
            final var source = new InputSource(getClass().getResourceAsStream(dataset));
            final var producer = new FlatXmlProducer(source, false, true);
            final var dataSet = new ReplacementDataSet(new FlatXmlDataSet(producer));

            dataSet.addReplacementObject("[NULL]", null);

            return dataSet;
        } catch (final DataSetException exception) {
            throw new RuntimeException("Cannot read the dataset file " + dataset + "!", exception);
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
