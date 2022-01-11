package org.httpfeeds.serverexample;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import org.hamcrest.Matchers;
import org.httpfeeds.server.JdbcFeedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class IntegrationTests {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  JdbcFeedRepository feedRepository;

  @BeforeEach
  void setUp() {
    feedRepository.deleteAll();
  }

  @Test
  void shouldRetrieveFirstBatch() throws Exception {
    prepareFeedItems();

    mockMvc.perform(get("/inventory")).andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/cloudevents-batch+json"))
        .andExpect(jsonPath("$.[0].id", Matchers.notNullValue()))
        .andExpect(content().json("[{\n" +
                                  "  \"specversion\" : \"1.0\",\n" +
                                  "  \"type\" : \"org.http-feeds.example.inventory\",\n" +
                                  "  \"id\" : \"1ec6fbad-9f2a-66f9-b0bb-fd0c9b604e85\",\n" +
                                  "  \"source\" : \"https://example.http-feeds.org/inventory\",\n" +
                                  "  \"time\" : \"2021-01-01T00:00:01Z\",\n" +
                                  "  \"subject\" : \"9521234567899\",\n" +
                                  "  \"data\" : {\n" +
                                  "    \"sku\": \"9521234567899\",\n" +
                                  "    \"updated\": \"2022-01-01T00:00:01Z\",\n" +
                                  "    \"quantity\": 5\n" +
                                  "  }\n" +
                                  "},{\n" +
                                  "  \"specversion\" : \"1.0\",\n" +
                                  "  \"type\" : \"org.http-feeds.example.inventory\",\n" +
                                  "  \"id\" : \"1ec6fbad-9f3d-6f7a-b0bb-fda3a29beb9c\",\n" +
                                  "  \"source\" : \"https://example.http-feeds.org/inventory\",\n" +
                                  "  \"time\" : \"2021-12-01T00:00:15Z\",\n" +
                                  "  \"subject\" : \"9521234512349\",\n" +
                                  "  \"data\" : {\n" +
                                  "    \"sku\": \"9521234512349\",\n" +
                                  "    \"updated\": \"2022-01-01T00:00:12Z\",\n" +
                                  "    \"quantity\": 0\n" +
                                  "  }\n" +
                                  "},{\n" +
                                  "  \"specversion\" : \"1.0\",\n" +
                                  "  \"type\" : \"org.http-feeds.example.inventory\",\n" +
                                  "  \"id\" : \"1ec6fbad-9f40-668b-b0bb-4f65b1c13ee1\",\n" +
                                  "  \"source\" : \"https://example.http-feeds.org/inventory\",\n" +
                                  "  \"time\" : \"2021-01-01T00:00:22Z\",\n" +
                                  "  \"subject\" : \"9521234567899\",\n" +
                                  "  \"data\" : {\n" +
                                  "    \"sku\": \"9521234567899\",\n" +
                                  "    \"updated\": \"2022-01-01T00:00:21Z\",\n" +
                                  "    \"quantity\": 4\n" +
                                  "  }\n" +
                                  "}]"));
  }

  @Test
  void shouldRetrieveBatchFromALastEventId() throws Exception {
    prepareFeedItems();

    mockMvc.perform(get("/inventory").queryParam("lastEventId", "1ec6fbad-9f3d-6f7a-b0bb-fda3a29beb9c"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/cloudevents-batch+json"))
        .andExpect(jsonPath("$.[0].id", Matchers.notNullValue()))
        .andExpect(content().json("[{\n" +
                                  "  \"specversion\" : \"1.0\",\n" +
                                  "  \"type\" : \"org.http-feeds.example.inventory\",\n" +
                                  "  \"id\" : \"1ec6fbad-9f40-668b-b0bb-4f65b1c13ee1\",\n" +
                                  "  \"source\" : \"https://example.http-feeds.org/inventory\",\n" +
                                  "  \"time\" : \"2021-01-01T00:00:22Z\",\n" +
                                  "  \"subject\" : \"9521234567899\",\n" +
                                  "  \"data\" : {\n" +
                                  "    \"sku\": \"9521234567899\",\n" +
                                  "    \"updated\": \"2022-01-01T00:00:21Z\",\n" +
                                  "    \"quantity\": 4\n" +
                                  "  }\n" +
                                  "}]"));

  }

  @Test
  void shouldGetEmptyResultWhenNoNewerEventIsPresent() throws Exception {
    prepareFeedItems();

    mockMvc.perform(get("/inventory").queryParam("lastEventId", "1ec6fbad-9f40-668b-b0bb-4f65b1c13ee1")).andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/cloudevents-batch+json"))
        .andExpect(content().json("[]"));
  }


  @Test
  void shouldRetrieveBatchWithRemovedEventId() throws Exception {
    prepareFeedItems();

    mockMvc.perform(get("/inventory").queryParam("lastEventId", "1ec6fbad-9f3d-6f70-b0bb-fda3a29beb9c")).andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/cloudevents-batch+json"))
        .andExpect(content().json("[{\n" +
                                  "  \"specversion\" : \"1.0\",\n" +
                                  "  \"type\" : \"org.http-feeds.example.inventory\",\n" +
                                  "  \"id\" : \"1ec6fbad-9f3d-6f7a-b0bb-fda3a29beb9c\",\n" +
                                  "  \"source\" : \"https://example.http-feeds.org/inventory\",\n" +
                                  "  \"time\" : \"2021-12-01T00:00:15Z\",\n" +
                                  "  \"subject\" : \"9521234512349\",\n" +
                                  "  \"data\" : {\n" +
                                  "    \"sku\": \"9521234512349\",\n" +
                                  "    \"updated\": \"2022-01-01T00:00:12Z\",\n" +
                                  "    \"quantity\": 0\n" +
                                  "  }\n" +
                                  "},{\n" +
                                  "  \"specversion\" : \"1.0\",\n" +
                                  "  \"type\" : \"org.http-feeds.example.inventory\",\n" +
                                  "  \"id\" : \"1ec6fbad-9f40-668b-b0bb-4f65b1c13ee1\",\n" +
                                  "  \"source\" : \"https://example.http-feeds.org/inventory\",\n" +
                                  "  \"time\" : \"2021-01-01T00:00:22Z\",\n" +
                                  "  \"subject\" : \"9521234567899\",\n" +
                                  "  \"data\" : {\n" +
                                  "    \"sku\": \"9521234567899\",\n" +
                                  "    \"updated\": \"2022-01-01T00:00:21Z\",\n" +
                                  "    \"quantity\": 4\n" +
                                  "  }\n" +
                                  "}]"));
  }


  @Test
  void shouldRetrieveFirstBatchWithPolling() throws Exception {
    prepareFeedItems();

    mockMvc.perform(get("/inventory").queryParam("timeout", "500")).andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/cloudevents-batch+json"))
        .andExpect(jsonPath("$.[0].id", Matchers.notNullValue()))
        .andExpect(content().json("[{\n" +
                                  "  \"specversion\" : \"1.0\",\n" +
                                  "  \"type\" : \"org.http-feeds.example.inventory\",\n" +
                                  "  \"id\" : \"1ec6fbad-9f2a-66f9-b0bb-fd0c9b604e85\",\n" +
                                  "  \"source\" : \"https://example.http-feeds.org/inventory\",\n" +
                                  "  \"time\" : \"2021-01-01T00:00:01Z\",\n" +
                                  "  \"subject\" : \"9521234567899\",\n" +
                                  "  \"data\" : {\n" +
                                  "    \"sku\": \"9521234567899\",\n" +
                                  "    \"updated\": \"2022-01-01T00:00:01Z\",\n" +
                                  "    \"quantity\": 5\n" +
                                  "  }\n" +
                                  "},{\n" +
                                  "  \"specversion\" : \"1.0\",\n" +
                                  "  \"type\" : \"org.http-feeds.example.inventory\",\n" +
                                  "  \"id\" : \"1ec6fbad-9f3d-6f7a-b0bb-fda3a29beb9c\",\n" +
                                  "  \"source\" : \"https://example.http-feeds.org/inventory\",\n" +
                                  "  \"time\" : \"2021-12-01T00:00:15Z\",\n" +
                                  "  \"subject\" : \"9521234512349\",\n" +
                                  "  \"data\" : {\n" +
                                  "    \"sku\": \"9521234512349\",\n" +
                                  "    \"updated\": \"2022-01-01T00:00:12Z\",\n" +
                                  "    \"quantity\": 0\n" +
                                  "  }\n" +
                                  "},{\n" +
                                  "  \"specversion\" : \"1.0\",\n" +
                                  "  \"type\" : \"org.http-feeds.example.inventory\",\n" +
                                  "  \"id\" : \"1ec6fbad-9f40-668b-b0bb-4f65b1c13ee1\",\n" +
                                  "  \"source\" : \"https://example.http-feeds.org/inventory\",\n" +
                                  "  \"time\" : \"2021-01-01T00:00:22Z\",\n" +
                                  "  \"subject\" : \"9521234567899\",\n" +
                                  "  \"data\" : {\n" +
                                  "    \"sku\": \"9521234567899\",\n" +
                                  "    \"updated\": \"2022-01-01T00:00:21Z\",\n" +
                                  "    \"quantity\": 4\n" +
                                  "  }\n" +
                                  "}]"));
  }

  @Test
  void shouldGetEmptyResultWhenNoNewerEventIsPresentWithPolling() throws Exception {
    prepareFeedItems();

    mockMvc.perform(get("/inventory")
            .queryParam("lastEventId", "1ec6fbad-9f40-668b-b0bb-4f65b1c13ee1")
            .queryParam("timeout", "500"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/cloudevents-batch+json"))
        .andExpect(content().json("[]"));
  }

  @Test
  void shouldRespectAcceptHeader_json() throws Exception {
    prepareFeedItems();

    mockMvc.perform(get("/inventory").accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  void shouldRespectAcceptHeader_cloudeventsBatch() throws Exception {
    prepareFeedItems();

    mockMvc.perform(get("/inventory").accept("application/cloudevents-batch+json"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/cloudevents-batch+json"));
  }



  private void prepareFeedItems() {
    feedRepository.append("1ec6fbad-9f2a-66f9-b0bb-fd0c9b604e85", "org.http-feeds.example.inventory",
        "https://example.http-feeds.org/inventory", Instant.parse("2021-01-01T00:00:01Z"),
        "9521234567899", null, "{\n" +
                               "  \"sku\": \"9521234567899\",\n" +
                               "  \"updated\": \"2022-01-01T00:00:01Z\",\n" +
                               "  \"quantity\": 5\n" +
                               "}");
    feedRepository.append("1ec6fbad-9f3d-6f7a-b0bb-fda3a29beb9c", "org.http-feeds.example.inventory",
        "https://example.http-feeds.org/inventory", Instant.parse("2021-12-01T00:00:15Z"),
        "9521234512349", null, "{\n" +
                               "  \"sku\": \"9521234512349\",\n" +
                               "  \"updated\": \"2022-01-01T00:00:12Z\",\n" +
                               "  \"quantity\": 0\n" +
                               "}");
    feedRepository.append("1ec6fbad-9f40-668b-b0bb-4f65b1c13ee1", "org.http-feeds.example.inventory",
        "https://example.http-feeds.org/inventory", Instant.parse("2021-01-01T00:00:22Z"),
        "9521234567899", null, "{\n" +
                               "  \"sku\": \"9521234567899\",\n" +
                               "  \"updated\": \"2022-01-01T00:00:21Z\",\n" +
                               "  \"quantity\": 4\n" +
                               "}");
  }
}
