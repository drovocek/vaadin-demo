package edu.volkov.events.data.generator;

import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import edu.volkov.events.data.entity.Person;
import edu.volkov.events.data.service.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(PersonRepository samplePersonRepository) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (samplePersonRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");

            logger.info("... generating 100 Sample Person entities...");
            ExampleDataGenerator<Person> samplePersonRepositoryGenerator = new ExampleDataGenerator<>(
                    Person.class, LocalDateTime.of(2021, 11, 28, 0, 0, 0));
            samplePersonRepositoryGenerator.setData(Person::setId, DataType.ID);
            samplePersonRepositoryGenerator.setData(Person::setFirstName, DataType.FIRST_NAME);
            samplePersonRepositoryGenerator.setData(Person::setLastName, DataType.LAST_NAME);
            samplePersonRepositoryGenerator.setData(Person::setEmail, DataType.EMAIL);
            samplePersonRepository.saveAll(samplePersonRepositoryGenerator.create(100, seed));

            logger.info("Generated demo data");
        };
    }

}