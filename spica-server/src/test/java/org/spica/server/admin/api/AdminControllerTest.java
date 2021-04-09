package org.spica.server.admin.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.spica.SpicaServerApplication;
import org.spica.commons.SpicaProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { SpicaServerApplication.class })
@Slf4j
public class AdminControllerTest {

  protected MockMvc mockMvc;

  @Autowired
  protected WebApplicationContext context;

  @BeforeAll
  public static void beforeClass () throws IOException {
    SpicaProperties.close();
  }

  @AfterAll
  public static void afterClass () {
    SpicaProperties spicaProperties = new SpicaProperties();
    spicaProperties.getCustomPropertiesFile().delete();

    SpicaProperties.close();
  }

  @BeforeEach
  public void setUp() throws IOException {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
  }

  @Test
  public void getCustomProperties () throws Exception {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    ResultActions resultActions = mockMvc.perform(get("/api/admin/customproperties")).andExpect(status().isOk());
    resultActions.andReturn().getResponse().getContentAsString();
  }

  @Test
  public void setCustomProperties () throws Exception {

    SpicaProperties spicaProperties = new SpicaProperties();
    Assert.assertFalse ("Custom Properties file exists", spicaProperties.getCustomPropertiesFile().exists());
    SpicaProperties.close();

    MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    HashMap<String, String> body = new HashMap<>();
    body.put("custom1", "value1");
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
    String requestJson=ow.writeValueAsString(body);
    mockMvc.perform(put("/api/admin/customproperties").contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isOk());
    spicaProperties = new SpicaProperties();
    Properties properties = spicaProperties.getCustomProperties();
    Assert.assertTrue (spicaProperties.getCustomPropertiesFile().delete());
    Assert.assertEquals ("value1", properties.getProperty("custom1"));

  }
}