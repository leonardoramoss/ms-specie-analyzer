package io.species.analyzer;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static io.species.analyzer.configuration.fixtures.JsonFixture.loadJsonFile;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration
@AutoConfigureMockMvc
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.JVM)
public class ActuatorIntegrationTests {

    @Autowired
    protected MockMvc mockMvc;

    @Test
    void givenHealthStatus_whenPerformRequest_shouldBeReturnStatusOfPeripheralsAndIntegrationsAndStatusOk() throws Exception {
        final var mvcResult = mockMvc.perform(get("/health").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();
        assertEquals(loadJsonFile("expected/health/expected_health.json"), mvcResult.getResponse().getContentAsString(), true);
    }

    @Test
    void givenHealthLiveness_whenPerformRequest_shouldBeReturnStatusOk() throws Exception {
        final var mvcResult = mockMvc.perform(get("/health/liveness").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();
        assertEquals(loadJsonFile("expected/health/expected_health_up_status.json"), mvcResult.getResponse().getContentAsString(), true);
    }

    @Test
    void givenHealthReadiness_whenPerformRequest_shouldBeReturnStatusOk() throws Exception {
        final var mvcResult = mockMvc.perform(get("/health/readiness").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();
        assertEquals(loadJsonFile("expected/health/expected_health_up_status.json"), mvcResult.getResponse().getContentAsString(), true);
    }

    @Test
    @Disabled
    void givenInfoDeployment_whenPerformRequest_shouldBeReturnAppAndJavaInformationStatusOk() throws Exception {
        final var mvcResult = mockMvc.perform(get("/info").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();
        assertEquals(loadJsonFile("expected/health/expected_info.json"), mvcResult.getResponse().getContentAsString(), ignoreFields("java.runtime-version"));
    }

    @NonNull
    private CustomComparator ignoreFields(final String ... paths) {
        return new CustomComparator(JSONCompareMode.STRICT,
                Arrays.stream(paths)
                    .map(path -> new Customization(path, (actual, expected) -> true))
                    .toArray(Customization[]::new));
    }
}
