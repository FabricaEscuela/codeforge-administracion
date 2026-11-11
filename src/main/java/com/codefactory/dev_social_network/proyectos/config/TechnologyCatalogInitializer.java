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
                    technology("Backend Developer", "ROL"), technology("Frontend Developer", "ROL"),
                    technology("Full Stack Developer", "ROL"), technology("DevOps Engineer", "ROL"),
                    technology("Java", "LENGUAJE"), technology("Python", "LENGUAJE"),
                    technology("JavaScript", "LENGUAJE"), technology("TypeScript", "LENGUAJE"),
                    technology("SQL", "LENGUAJE"),
                    technology("Spring Boot", "FRAMEWORK"), technology("React", "FRAMEWORK"),
                    technology("Angular", "FRAMEWORK"), technology("Django", "FRAMEWORK"),
                    technology("Docker", "FRAMEWORK")
            );
            try {
                repository.saveAll(seed);
            } catch (RuntimeException e) {
                // Con roles de minimo privilegio (solo SELECT sobre tecnologias) el catalogo
                // se carga desde seed.sql; no se aborta el arranque.
                org.slf4j.LoggerFactory.getLogger(TechnologyCatalogInitializer.class)
                        .warn("No se pudo sembrar el catalogo de tecnologias: {}", e.getMessage());
            }
        };
    }

    private static Technology technology(String name, String type) {
        Technology technology = new Technology();
        technology.setName(name);
        technology.setType(type);
        return technology;
    }
}