package ee.avok.consultation;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


@Configuration
public class DatabasePopulator {
    
	@Autowired
    ObjectMapper mapper;
	    @Bean
    public Jackson2RepositoryPopulatorFactoryBean repositoryPopulator() throws IOException {

        Resource sourceData = new ClassPathResource("sample-catalog.json");
        Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
        mapper.registerModule(new JavaTimeModule());
        factory.setMapper(mapper);
        factory.setResources(new Resource[] { sourceData });
        return factory;
    }
    
}