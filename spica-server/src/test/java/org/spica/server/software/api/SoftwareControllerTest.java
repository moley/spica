package org.spica.server.software.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.spica.SpicaServerApplication;
import org.spica.commons.SpicaProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { SpicaServerApplication.class })
public class SoftwareControllerTest {

  protected MockMvc mockMvc;

  @Autowired
  protected WebApplicationContext context;

  @BeforeClass
  public static void beforeClass () throws IOException {
    System.out.println (System.getProperty("java.version"));
    SpicaProperties.close();
    System.setProperty("demodata", "true");
  }

  @AfterClass
  public static void afterClass () {
    System.clearProperty("demodata");
    SpicaProperties.close();
  }

  @Before
  public void setUp() throws IOException {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
  }

  @Test
  public void getSoftware () throws Exception {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    ResultActions resultActions = mockMvc.perform(get("/api/software/list")).andExpect(status().isOk());
    String result = resultActions.andReturn().getResponse().getContentAsString();

    Assert.assertTrue ("Root software Spica not found", result.contains("Spica"));
    Assert.assertEquals ("Non root software Spica-CLI found", 1, StringUtils.countMatches(result, "Spica-CLI"));
  }
}
