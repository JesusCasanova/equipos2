package com.example.equipos.DAO;

import com.example.equipos.entities.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource
public interface EquipoRepository extends JpaRepository<Equipo, Long> {

}
