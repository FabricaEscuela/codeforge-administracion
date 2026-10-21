package com.codefactory.dev_social_network.proyectos.config;

import com.codefactory.dev_social_network.proyectos.entity.Technology;
import com.codefactory.dev_social_network.proyectos.repository.TechnologyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class TechnologyCatalogInitializer {

    @Bean
    CommandLineRunner seedProjectTechnologies(TechnologyRepository repository) {
        return args -> {
            if (repository.count() != 0) return;
            List<Technology> seed = List.of(
                    technology("Java", "LANGUAGE"), technology("JavaScript", "LANGUAGE"),
                    technology("TypeScript", "LANGUAGE"), technology("Python", "LANGUAGE"),
                    technology("C#", "LANGUAGE"), technology("Go", "LANGUAGE"),
                    technology("Kotlin", "LANGUAGE"), technology("Rust", "LANGUAGE"),
                    technology("SQL", "LANGUAGE"), technology("Swift", "LANGUAGE"),
                    technology("Spring Boot", "FRAMEWORK"), technology("React", "FRAMEWORK"),
                    technology("Angular", "FRAMEWORK"), technology("Vue", "FRAMEWORK"),
                    technology("Node.js", "FRAMEWORK"), technology("Django", "FRAMEWORK"),
                    technology(".NET", "FRAMEWORK"), technology("Flutter", "FRAMEWORK"),
                    technology("PostgreSQL", "DATABASE"), technology("Docker", "TOOL")
            );
            repository.saveAll(seed);
        };
    }

    private static Technology technology(String name, String type) {
        Technology technology = new Technology();
        technology.setName(name);
        technology.setType(type);
        return technology;
    }
}
