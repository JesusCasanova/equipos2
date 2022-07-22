package com.example.equipos.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "equipos")
@Data
@NoArgsConstructor @AllArgsConstructor @ToString
public class Equipo {

        /*
        hemos hecho una clase equipo a imagen de la t abla equipo. Ahora creams el paquete dao
        dentro del equipodao, hemnos creado la clase equipo repository. Luego el controlador dentro
        del paquete services
         */

    /*
    para emplear lombook, primero hay que añadir la dependencia en el pom, luego incluir las
    anotaciones Data y @NoArgsConstructor @AllArgsConstructor @ToString. Partimos de la base de que el
    plugin de lombok está ya instalado enel IDE
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(nullable=false, name="nombre")
    private String nombre;

    @Column(nullable=false, name="idliga")
    private long idliga;

    @Column(nullable=false, name="idnac")
    private long idnac;

    @Column(nullable=false, name="idestadio")
    private long idestadio;



}
