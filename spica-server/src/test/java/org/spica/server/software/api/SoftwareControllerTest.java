package org.spica.server.software.api;

import java.io.IOException;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.spica.SpicaServerApplication;
import org.spica.commons.SpicaProperties;
import org.spica.server.demodata.DemoDataCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { SpicaServerApplication.class })
public class SoftwareControllerTest {

  protected MockMvc mockMvc;

  @Autowired
  protected WebApplicationContext context;

  @BeforeAll
  public static void beforeClass (@Autowired DemoDataCreator demoDataCreator) throws IOException {
    SpicaProperties.close();
    demoDataCreator.createSoftware();
  }

  @AfterAll
  public static void afterClass () {
    SpicaProperties.close();
  }

  @BeforeEach
  public void setUp() throws IOException {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
  }

  @Test
  public void getSoftware () throws Exception {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    ResultActions resultActions = mockMvc.perform(get("/api/software/list")).andExpect(status().isOk());
    String result = resultActions.andReturn().getResponse().getContentAsString();

    Assertions.assertTrue (result.contains("Spica"), "Root software Spica not found");
    Assertions.assertEquals (1, StringUtils.countMatches(result, "Spica-CLI"), "Non root software Spica-CLI found");
  }
}
