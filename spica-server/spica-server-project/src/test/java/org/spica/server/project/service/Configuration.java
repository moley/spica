package org.spica.server.project.service;

import org.mockito.Mockito;
import org.spica.server.project.domain.TopicRepository;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@ComponentScan(basePackages = {"org.spica.server.project.service"})
public class Configuration {

    @Bean
    public TopicRepository topicRepository() {
        TopicRepository topicRepository = Mockito.mock(TopicRepository.class);
        return topicRepository;
    }




}
