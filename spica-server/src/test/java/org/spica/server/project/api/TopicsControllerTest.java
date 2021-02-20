package org.spica.server.project.api;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.spica.SpicaServerApplication;
import org.spica.server.project.domain.Task;
import org.spica.server.project.domain.TaskRepository;
import org.spica.server.project.service.TaskImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.containsString;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SpicaServerApplication.class})
public class TopicsControllerTest {

    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext context;

    @MockBean
    private TaskImporter mockedJiraTaskImporter;

    @MockBean
    private TaskRepository mockedTaskRepository;

    @Before
    public void setUp() throws InterruptedException {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        Task task = Task.builder().name("TOPIC").build();
        Mockito.when(mockedJiraTaskImporter.importTasksOfUser(Mockito.eq("user"), Mockito.eq("spica"), Mockito.eq("spicaPwd"))).thenReturn(Arrays.asList(
            task));
    }

    @Test
    public void importFromJira () throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/topics/import/user?usernameExternalSystem=spica&passwordExternalSystem=spicaPwd")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().string(containsString("\"topics\":[{\"id\"")));

    }


}
