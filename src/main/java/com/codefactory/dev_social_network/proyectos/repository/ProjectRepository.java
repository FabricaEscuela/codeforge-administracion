package com.codefactory.dev_social_network.proyectos.repository;

import com.codefactory.dev_social_network.proyectos.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
}