package com.codefactory.dev_social_network.proyectos.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codefactory.dev_social_network.proyectos.repository.TechnologyRepository;

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

    public record TechnologyOption(UUID id, String name, String type) {}
}