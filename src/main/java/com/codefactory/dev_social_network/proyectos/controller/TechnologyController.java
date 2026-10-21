package com.codefactory.dev_social_network.proyectos.controller;

import com.codefactory.dev_social_network.proyectos.entity.Technology;
import com.codefactory.dev_social_network.proyectos.repository.TechnologyRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/technologies")
public class TechnologyController {

    private final TechnologyRepository technologyRepository;

    public TechnologyController(TechnologyRepository technologyRepository) {
        this.technologyRepository = technologyRepository;
    }

    @GetMapping
    public List<TechnologyOption> list() {
        return technologyRepository.findAll().stream()
                .map(technology -> new TechnologyOption(technology.getId(), technology.getName(), technology.getType()))
                .toList();
    }

    public record TechnologyOption(Long id, String name, String type) {}
}
