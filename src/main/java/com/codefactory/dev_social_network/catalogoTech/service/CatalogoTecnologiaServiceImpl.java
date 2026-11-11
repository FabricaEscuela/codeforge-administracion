package com.codefactory.dev_social_network.catalogoTech.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.codefactory.dev_social_network.catalogoTech.interfaces.CatalogoTecnologiaApi;
import com.codefactory.dev_social_network.proyectos.repository.TechnologyRepository;

@Service
public class CatalogoTecnologiaServiceImpl implements CatalogoTecnologiaApi {

    private final TechnologyRepository technologyRepository;

    public CatalogoTecnologiaServiceImpl(TechnologyRepository technologyRepository) {
        this.technologyRepository = technologyRepository;
    }

    @Override
    public boolean isTecnologyAvailable(List<String> nameTecnology) {
        if (nameTecnology == null) {
            return true;
        }
        return nameTecnology.stream()
                .allMatch(nombre -> technologyRepository
                        .existsByNameIgnoreCase(nombre == null ? "" : nombre.trim()));
    }

    @Override
    public List<String> obtenerNombresDisponibles() {
        return technologyRepository.findAll()
                .stream()
                .map(tecnologia -> tecnologia.getName())
                .toList();
    }

    public boolean existe(String nameTecnology) {
        if (nameTecnology == null || nameTecnology.isBlank()) {
            return false;
        }
        return technologyRepository.existsByNameIgnoreCase(nameTecnology.trim());
    }
}